package com.peng.pinyougou.search.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.peng.pinyougou.search.service.ItemSearchService;

/**   
* 项目名称：pinyougou-search-service   
* 类名称：ItemDeleteListener   
* 类描述：   接收activeMQ消息删除索引库
* 创建人：彭坤   
* 创建时间：2018年9月17日 下午9:20:54      
* @version     
*/
@Component
public class ItemDeleteListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage objectMessage=(ObjectMessage)message;
			Long[] ids=(Long[]) objectMessage.getObject();
			List<Long> asList = Arrays.asList(ids);
			itemSearchService.deleteByGoodsIds(asList);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
