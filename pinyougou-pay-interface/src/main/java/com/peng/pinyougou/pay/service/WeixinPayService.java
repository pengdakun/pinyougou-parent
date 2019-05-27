package com.peng.pinyougou.pay.service;

import java.util.Map;

/**   
* 项目名称：pinyougou-pay-interface   
* 类名称：WeixinPayService   
* 类描述：   微信支付service
* 创建人：彭坤   
* 创建时间：2018年9月29日 下午9:23:58      
* @version     
*/
public interface WeixinPayService {
	
	/** 
	 * @param @param outTradeNo
	 * @param @param totalFee
	 * @param @return 
	 * @return Map<String,String>  
	 * @Description 生成二维码返回支付地址
	 * @author 彭坤
	 * @date 2018年9月29日 下午9:28:26
	 */
	public Map<String, String> caretNative(String outTradeNo,String totalFee);
	
	
	/** 
	 * @param @param orderId
	 * @param @return 
	 * @return Map<String,String>  
	 * @Description 查询支付状态
	 * @author 彭坤
	 * @date 2018年10月1日 下午1:14:53
	 */
	public Map<String, String> queryPayStatus(String orderId);
	
	/** 
	 * @param @param out_trade_no
	 * @param @return 
	 * @return Map<String,String>  
	 * @Description 关闭订单
	 * @author 彭坤
	 * @date 2018年10月2日 下午3:40:00
	 */
	public Map<String, String> closePay(String out_trade_no);

}
