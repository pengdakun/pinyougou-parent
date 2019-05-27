package com.peng.pinyougou.search.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.peng.pinyougou.search.service.ItemSearchService;

/**   
* 项目名称：pinyougou-search-web   
* 类名称：SearchController   
* 类描述：   搜索Controller
* 创建人：彭坤   
* 创建时间：2018年9月5日 下午9:44:48      
* @version     
*/
@RestController
@RequestMapping(value="/itemSearch")
public class ItemSearchController {

	@Reference
	private ItemSearchService itemSearchService;
	
	/** 
	 * @param @param searchMap
	 * @param @return 
	 * @return Map  
	 * @Description 搜索
	 * @author 彭坤
	 * @date 2018年9月5日 下午9:47:35
	 */
	@RequestMapping(value="/search")
	public Map search(@RequestBody Map searchMap){
		return itemSearchService.search(searchMap);
	}
	
}
