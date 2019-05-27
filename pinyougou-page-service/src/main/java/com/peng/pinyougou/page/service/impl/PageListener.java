package com.peng.pinyougou.page.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.peng.pimyougou.page.service.ItemPageService;

/**   
* 项目名称：pinyougou-page-service   
* 类名称：PageListener   
* 类描述：   接收消息，生成静态文件
* 创建人：彭坤   
* 创建时间：2018年9月17日 下午9:53:24      
* @version     
*/
@Component
public class PageListener implements MessageListener {
	
	@Autowired
	private ItemPageService itemPageService;
	

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage=(TextMessage)message;
			String id = textMessage.getText();
			itemPageService.genItemHtml(Long.parseLong(id));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
