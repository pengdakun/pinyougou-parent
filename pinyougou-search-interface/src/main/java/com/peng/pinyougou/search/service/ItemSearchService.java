package com.peng.pinyougou.search.service;

import java.util.List;
import java.util.Map;

import com.peng.pinyougou.pojo.TbItem;

/**   
* 项目名称：pinyougou-search-interface   
* 类名称：ItemSearchService   
* 类描述：   商品搜索
* 创建人：彭坤   
* 创建时间：2018年9月5日 下午9:07:52      
* @version     
*/
public interface ItemSearchService {

	
	/** 
	 * @param @param searchMap
	 * @param @return 
	 * @return Map  
	 * @Description 搜索方法 
	 * @author 彭坤
	 * @date 2018年9月5日 下午9:10:38
	 */
	public Map search(Map searchMap);
	
	
	/** 
	 * @param  
	 * @return void  
	 * @Description 将sku集合加入索引库
	 * @author 彭坤
	 * @date 2018年9月9日 下午8:42:32
	 */
	public void importListItem(List<TbItem> tbItems);
	
	
	/** 
	 * @param @param goodsIds 
	 * @return void  
	 * @Description 通过spu的ids删除sku列表
	 * @author 彭坤
	 * @date 2018年9月9日 下午9:11:02
	 */
	public void deleteByGoodsIds(List goodsIds);
	
}
