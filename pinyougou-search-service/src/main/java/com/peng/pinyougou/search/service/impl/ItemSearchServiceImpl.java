package com.peng.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.w3c.dom.stylesheets.LinkStyle;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.peng.pinyougou.pojo.TbItem;
import com.peng.pinyougou.search.service.ItemSearchService;

@Service(timeout=5000)//设置dubbo调用超时时间
public class ItemSearchServiceImpl implements ItemSearchService {

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public Map search(Map searchMap) {
		
		Map<Object, Object> hashMap = new HashMap<>();
		
		//去除空格
		String keywords = (String) searchMap.get("keywords");
		searchMap.put("keywords", keywords.replace(" ", ""));
		
		
		hashMap.putAll(searchList(searchMap));
		//获得分组列表
		List<String> categoryList = searchCategoryList(searchMap);
		hashMap.put("categoryList", categoryList);
		//通过第一个分类名称获得品牌及规格列表
		if (categoryList!=null && categoryList.size()>0) {
			String category = (String) searchMap.get("category");
			Map searchBrandSpec=null;
			if(category!=null && !"".equals(category)){
				searchBrandSpec = searchBrandSpec(category);
			}else{
				searchBrandSpec = searchBrandSpec(categoryList.get(0));
			}
			hashMap.putAll(searchBrandSpec);
		}
		return hashMap;
	}
	
	//高亮查询
	private Map searchList(Map searchMap){
		Map<Object, Object> hashMap = new HashMap<>();
		//高亮选项初始化
		HighlightQuery query=new SimpleHighlightQuery();
		HighlightOptions highlightOptions=new HighlightOptions();
		//设置高亮显示列
		highlightOptions.addField("item_title");
		//设置前后缀
		highlightOptions.setSimplePrefix("<em style='color:red;'>");
		highlightOptions.setSimplePostfix("</em>");
		query.setHighlightOptions(highlightOptions);
		
		//构建查询  按照关键字查询
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		//按照商品分类查询
		String category = (String) searchMap.get("category");
		if(category!=null && !"".equals(category)){
			FilterQuery filterQuery=new SimpleFilterQuery();
			Criteria filterCriteria=new Criteria("item_category").is(category);
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		//根据品牌查询
		String brand = (String) searchMap.get("brand");
		if(brand!=null && !"".equals(brand)){
			FilterQuery filterQuery=new SimpleFilterQuery();
			Criteria filterCriteria=new Criteria("item_brand").is(brand);
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		//根据规格过滤
		Object object = searchMap.get("spec");
		if (object!=null) {
			Map<String,String> parseObject = (Map<String, String>) searchMap.get("spec");
			for(String key:parseObject.keySet()){
				FilterQuery filterQuery=new SimpleFilterQuery();
				Criteria filterCriteria=new Criteria("item_spec_"+key).is(parseObject.get(key));
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}
		
		//根据价格查询
		String price = (String) searchMap.get("price");//500-1000
		if (price!=null && !"".equals(price)) {
			String[] priceArray = price.split("-");
			if (!priceArray[0].equals("0")) {//如果起始价格不等于0
				FilterQuery filterQuery=new SimpleFilterQuery();
				Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(priceArray[0]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
			if (!priceArray[1].equals("*")) {//如果终止价格不等于*
				FilterQuery filterQuery=new SimpleFilterQuery();
				Criteria filterCriteria=new Criteria("item_price").lessThanEqual(priceArray[1]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}
		
		//分页
		Integer pageNo = (Integer) searchMap.get("pageNo");//页码
		if(pageNo==null){
			pageNo=1;
		}
		Integer pageSize = (Integer) searchMap.get("pageSize");//每页显示条数
		if (pageSize==null) {
			pageSize=20;
		}
		query.setOffset((pageNo-1)*pageSize);//设置起始索引
		query.setRows(pageSize);//设置每页记录数
		
		//价格排序
		String sortValue = (String) searchMap.get("sort");//排序方式
		String sortField = (String) searchMap.get("sortField");//排序字段
		if(sortValue!=null && !"".equals(sortValue)){
			Sort sort=null;
			if (sortValue.equals("asc")) {
				sort=new Sort(Sort.Direction.ASC, "item_"+sortField);
			}else{
				sort=new Sort(Sort.Direction.DESC, "item_"+sortField);
			}
			query.addSort(sort);
		}
		
		
		//获得高亮结果集
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		//获的高亮入口集合(每一条记录)
		List<HighlightEntry<TbItem>> highlighted = page.getHighlighted();
		for (HighlightEntry<TbItem> highlightEntry : highlighted) {
			//获的高亮列表(每一个字段)
			List<Highlight> highlights = highlightEntry.getHighlights();
			/*for (Highlight highlight : highlights) {
				List<String> snipplets = highlight.getSnipplets();
				System.out.println(snipplets);
			}*/
			if (highlights.size()>0 && highlights.get(0).getSnipplets().size()>0) {
				TbItem entity = highlightEntry.getEntity();
				entity.setTitle(highlights.get(0).getSnipplets().get(0));
			}
		}
		hashMap.put("rows", page.getContent());
		hashMap.put("totalPages", page.getTotalPages());//返回总页数
		hashMap.put("total", page.getTotalElements());//返回总记录数
		return hashMap;
	}
	
	
	
	/** 
	 * @param @return 
	 * @return List  
	 * @Description 分组查询 查询分类列表
	 * @author 彭坤
	 * @date 2018年9月8日 下午12:55:04
	 */
	private List<String> searchCategoryList(Map searchMap){
		Query query=new SimpleQuery("*:*");
		//设置查询条件
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		//设置分组查询
		GroupOptions options=new GroupOptions();
		//设置分组列
		options.addGroupByField("item_category");
		query.setGroupOptions(options);
		//获取分主页
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		//获得分组结果
		GroupResult<TbItem> result = page.getGroupResult("item_category");
		//获取分组入口页
		Page<GroupEntry<TbItem>> groupEntries = result.getGroupEntries();
		//获取分组入口集合
		List<GroupEntry<TbItem>> content = groupEntries.getContent();
		List<String> list = new ArrayList<String>();
		for (GroupEntry<TbItem> groupEntry : content) {
			//获得分组结构
			list.add(groupEntry.getGroupValue());
		}
		return list;
	}
	
	/** 
	 * @param @return 
	 * @return Map  
	 * @Description 从redis中获得品牌及规格数据
	 * @author 彭坤
	 * @date 2018年9月8日 下午1:54:56
	 */
	private Map searchBrandSpec(String categoryName){
		Map<Object,Object> map = new  HashMap<>();
		//通过分类名称获得模板id
		Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(categoryName);
		if (templateId!=null) {
			//通过模板id获得品牌列表
			List brandList=(List) redisTemplate.boundHashOps("barndList").get(templateId);
			map.put("brandList", brandList);
			//通过模板id获得规格列表
			List specList=(List) redisTemplate.boundHashOps("specList").get(templateId);
			map.put("specList", specList);
		}
		
		return map;
	}

	@Override
	public void importListItem(List<TbItem> tbItems) {
		solrTemplate.saveBeans(tbItems);
		solrTemplate.commit();
	}

	@Override
	public void deleteByGoodsIds(List goodsIds) {
		SolrDataQuery query=new SimpleQuery("*:*");
		Criteria criteria=new Criteria("item_goodsid").in(goodsIds);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
	}

}
