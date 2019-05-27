package com.peng.pimyougou.page.service;

public interface ItemPageService {
	
	/** 
	 * @param @param goodsId
	 * @param @return 
	 * @return boolean  
	 * @Description 生成商品详情页
	 * @author 彭坤
	 * @date 2018年9月10日 下午9:46:05
	 */
	public boolean genItemHtml(Long goodsId);
	
	
	/** 
	 * @param @param goodsIds
	 * @param @return 
	 * @return boolean  
	 * @Description 删除商品详情页
	 * @author 彭坤
	 * @date 2018年9月18日 下午8:38:23
	 */
	public boolean deleteItemHtml(Long[] goodsIds);
	

}
