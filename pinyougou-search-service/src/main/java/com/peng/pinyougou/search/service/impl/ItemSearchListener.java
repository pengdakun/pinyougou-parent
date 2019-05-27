package com.peng.pinyougou.search.service.impl;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.peng.pinyougou.pojo.TbItem;
import com.peng.pinyougou.search.service.ItemSearchService;

/**   
* 项目名称：pinyougou-search-service   
* 类名称：ItemSearchListener   
* 类描述：   接收activiteMQ消息
* 创建人：彭坤   
* 创建时间：2018年9月17日 下午9:05:51      
* @version     
*/
@Component
public class ItemSearchListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage=(TextMessage)message;
			String text = textMessage.getText();
			List<TbItem> parseArray = JSON.parseArray(text, TbItem.class);
			itemSearchService.importListItem(parseArray);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		
	}

}
