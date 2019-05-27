package com.peng.pinyougou.page.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.peng.pimyougou.page.service.ItemPageService;
import com.peng.pinyougou.mapper.TbGoodsDescMapper;
import com.peng.pinyougou.mapper.TbGoodsMapper;
import com.peng.pinyougou.mapper.TbItemCatMapper;
import com.peng.pinyougou.mapper.TbItemMapper;
import com.peng.pinyougou.pojo.TbGoods;
import com.peng.pinyougou.pojo.TbGoodsDesc;
import com.peng.pinyougou.pojo.TbItem;
import com.peng.pinyougou.pojo.TbItemExample;
import com.peng.pinyougou.pojo.TbItemExample.Criteria;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**   
* 项目名称：pinyougou-page-service   
* 类名称：ItemPageServiceImpl   
* 类描述：   生成静态网页
* 创建人：彭坤   
* 创建时间：2018年9月10日 下午9:59:06      
* @version     
*/
@Service
public class ItemPageServiceImpl implements ItemPageService {
	
	@Autowired
	private FreeMarkerConfigurer freemarkerConfig;
	
	@Value("${pagedir}")
	private String pagedir;
	
	@Autowired
	private TbGoodsMapper tbGoodsMapper;
	
	@Autowired
	private TbGoodsDescMapper tbGoodsDescMapper;
	
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Autowired
	private TbItemMapper tbItemMapper;

	@Override
	public boolean genItemHtml(Long goodsId) {
		try {
			Configuration configuration = freemarkerConfig.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//创建数据模型
			Map<Object,Object> map = new HashMap<>();
			TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodsId);
			TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(goodsId);
			map.put("goods", tbGoods);
			map.put("goodsDesc", tbGoodsDesc);
			
			//读取商品分类
			String itemCat1 = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
			String itemCat2 = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
			String itemCat3 = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();
			map.put("itemCat1", itemCat1);
			map.put("itemCat2", itemCat2);
			map.put("itemCat3", itemCat3);
			
			//查询商品sku
			TbItemExample example=new TbItemExample();
			Criteria createCriteria = example.createCriteria();
			createCriteria.andGoodsIdEqualTo(goodsId);
			createCriteria.andStatusEqualTo("1");
			example.setOrderByClause("is_default desc");//返回第一条结果为默认sku
			List<TbItem> selectByExample = tbItemMapper.selectByExample(example);
			map.put("itemList", selectByExample);
			//进行文件输出
			Writer writer = new FileWriter(new File(pagedir+goodsId+".html"));
			template.process(map, writer);
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}

	@Override
	public boolean deleteItemHtml(Long[] goodsIds) {
		try {
			for (Long id : goodsIds) {
				new File(pagedir+id+".html").delete();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
