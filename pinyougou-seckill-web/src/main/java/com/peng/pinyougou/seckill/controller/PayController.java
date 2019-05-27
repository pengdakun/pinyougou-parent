package com.peng.pinyougou.seckill.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.peng.pinyougou.pay.service.WeixinPayService;
import com.peng.pinyougou.pojo.TbSeckillOrder;
import com.peng.pinyougou.seckill.service.SeckillOrderService;

import entity.Result;

/**   
* 项目名称：pinyougou-cart-web   
* 类名称：PayController   
* 类描述：   支付controller
* 创建人：彭坤   
* 创建时间：2018年9月29日 下午10:11:51      
* @version     
*/
@RestController
@RequestMapping("/pay")
public class PayController {
	
	@Reference
	private WeixinPayService weixinPayService;
	
	@Reference
	private SeckillOrderService seckillOrderService;
	
	/** 
	 * @param @return 
	 * @return Map<String,String>  
	 * @Description 创建二维码
	 * @author 彭坤
	 * @date 2018年10月1日 下午1:32:30
	 */
	@RequestMapping("/createNative")
	public Map<String, String> createNative(){
		//获得用户秒杀订单
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		TbSeckillOrder tbSeckillOrder = seckillOrderService.searchOrderFromRedisByUserId(name);
		if(tbSeckillOrder!=null){
			//获得微信支付链接
			return weixinPayService.caretNative(tbSeckillOrder.getId()+"", (long)(tbSeckillOrder.getMoney().doubleValue()*100)+"");
		}
		return new HashMap<>();
	}
	
	/** 
	 * @param @param orderId
	 * @param @return 
	 * @return Result  
	 * @Description 查询订单状态
	 * @author 彭坤
	 * @date 2018年10月1日 下午1:32:41
	 */
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String orderId){
		//无限循环调用查询支付状态
		Result result=null;
		int x=0;
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		while(true){
			Map<String, String> map = weixinPayService.queryPayStatus(orderId);
			if (map==null) {
				result=new Result(false, "支付出现异常");
				break;
			}
			//获得交易状态
			String tradeState = map.get("trade_state");
			if (tradeState.equals("SUCCESS")) {
				result=new Result(true, "支付成功");
				//插入秒杀订单
				seckillOrderService.saveSecKillOrder(name,Long.parseLong(orderId),map.get("transaction_id"));
				break;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			x++;
			if(x>=100){//五分钟未支付则超时
				result=new Result(false, "二维码超时");
				Map<String, String> closePay = weixinPayService.closePay(orderId);
				if (closePay!=null && "SUCCESS".equals(closePay.get("return_code")) && "ORDERPAID".equals(closePay.get("err_code"))) {//关闭期间用户已支付
					result=new Result(true, "支付成功");
					seckillOrderService.saveSecKillOrder(name,Long.parseLong(orderId),map.get("transaction_id"));
				}
				if (result.isSuccess()) {
					seckillOrderService.deleteOrderFromRedis(name, Long.parseLong(orderId));
				}
				break;
			}
		}
		return result;
	}
	

}
