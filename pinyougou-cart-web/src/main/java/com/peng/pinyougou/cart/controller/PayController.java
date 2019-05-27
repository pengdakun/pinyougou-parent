package com.peng.pinyougou.cart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.peng.pinyougou.order.service.OrderService;
import com.peng.pinyougou.pay.service.WeixinPayService;
import com.peng.pinyougou.pojo.TbPayLog;
import com.peng.pinyougou.util.IdWorker;

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
	private OrderService orderService;
	
	/** 
	 * @param @return 
	 * @return Map<String,String>  
	 * @Description 创建二维码
	 * @author 彭坤
	 * @date 2018年10月1日 下午1:32:30
	 */
	@RequestMapping("/createNative")
	public Map<String, String> createNative(){
		//获得用户支付日志
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		TbPayLog tbPayLog = orderService.searchPayLogFromRedis(name);
		if(tbPayLog!=null){
			//获得微信支付链接
			return weixinPayService.caretNative(tbPayLog.getOutTradeNo(), tbPayLog.getTotalFee()+"");
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
				//修改支付状态
				orderService.updateOrderStatus(orderId, map.get("transaction_id"));
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
				break;
			}
		}
		return result;
	}
	

}
