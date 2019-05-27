package com.peng.pinyougou.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.peng.pinyougou.pay.service.WeixinPayService;
import com.peng.pinyougou.util.HttpClient;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

	@Value("${appid}")
	private String appid;
	@Value("${partner}")
	private String partner;
	@Value("${partnerkey}")
	private String partnerkey;
	@Value("${notifyurl}")
	private String notifyUrl;
	
	@Override
	public Map<String, String> caretNative(String outTradeNo, String totalFee) {
		try {
			//参数封装
			Map<String,String> map = new HashMap<String,String>();
			map.put("appid", appid);
			map.put("mch_id", partner);
			String nonceStr = WXPayUtil.generateNonceStr();
			map.put("nonce_str", nonceStr);
			map.put("body", "品优购");
			map.put("out_trade_no",outTradeNo);
			map.put("total_fee",totalFee);
			map.put("spbill_create_ip", "127.0.0.1");//IP
			map.put("notify_url", notifyUrl);//回调地址(随便写)
			map.put("trade_type", "NATIVE");
			String generateSignedXml = WXPayUtil.generateSignedXml(map, partnerkey);
			//发送请求
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			httpClient.setHttps(true);
			httpClient.setXmlParam(generateSignedXml);
			httpClient.post();
			//获取结果
			String content = httpClient.getContent();
			System.out.println(content);
			Map<String, String> xmlToMap = WXPayUtil.xmlToMap(content);
			Map<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("code_url", xmlToMap.get("code_url"));
			hashMap.put("outTradeNo", outTradeNo);
			hashMap.put("totalFee", totalFee);
			return hashMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, String> queryPayStatus(String orderId) {
		try {
			//封装参数
			Map<String,String> map = new HashMap<String,String>();
			map.put("appid", appid);
			map.put("mch_id", partner);
			map.put("out_trade_no", orderId);
			String nonceStr = WXPayUtil.generateNonceStr();
			map.put("nonce_str", nonceStr);
			String generateSignature = WXPayUtil.generateSignature(map, partnerkey);
			//发送请求
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			httpClient.setHttps(true);
			httpClient.setXmlParam(generateSignature);
			httpClient.post();
			//获得结果
			String content = httpClient.getContent();
			Map<String, String> xmlToMap = WXPayUtil.xmlToMap(content);
			return xmlToMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, String> closePay(String out_trade_no) {
		try {
			//封装参数
			Map<String,String> map = new HashMap<String,String>();
			map.put("appid", appid);
			map.put("mch_id", partner);
			map.put("out_trade_no", out_trade_no);
			String nonceStr = WXPayUtil.generateNonceStr();
			map.put("nonce_str", nonceStr);
			String generateSignature = WXPayUtil.generateSignature(map, partnerkey);
			//发送请求
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
			httpClient.setHttps(true);
			httpClient.setXmlParam(generateSignature);
			httpClient.post();
			//获得结果
			String content = httpClient.getContent();
			Map<String, String> xmlToMap = WXPayUtil.xmlToMap(content);
			return xmlToMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
