package com.peng.pinyougou.page.service.impl;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.peng.pimyougou.page.service.ItemPageService;

/**   
* 项目名称：pinyougou-page-service   
* 类名称：PageDeleteListener   
* 类描述：   接收消息，删除商品详情页
* 创建人：彭坤   
* 创建时间：2018年9月18日 下午8:40:53      
* @version     
*/
@Component
public class PageDeleteListener implements MessageListener {

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage objectMessage=(ObjectMessage)message;
			Long[] ids=(Long[]) objectMessage.getObject();
			itemPageService.deleteItemHtml(ids);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
