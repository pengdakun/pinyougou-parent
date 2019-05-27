package com.peng.pinyougou.solr.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.peng.pinyougou.mapper.TbItemMapper;
import com.peng.pinyougou.pojo.TbItem;
import com.peng.pinyougou.pojo.TbItemExample;
import com.peng.pinyougou.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {

	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	public void importItemData(){
		//查数据
		TbItemExample example=new TbItemExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andStatusEqualTo("1");//审核通过才导入
		List<TbItem> items = tbItemMapper.selectByExample(example);
		for (TbItem tbItem : items) {
			//提取规格转换为map，设置到solr动态域
			Map parseObject = JSON.parseObject(tbItem.getSpec(),Map.class);
			tbItem.setSpecMap(parseObject);
		}
		solrTemplate.saveBeans(items);
		solrTemplate.commit();
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = (com.peng.pinyougou.solr.util.SolrUtil) context.getBean("solrUtil");
		solrUtil.importItemData();
	}
	
}
