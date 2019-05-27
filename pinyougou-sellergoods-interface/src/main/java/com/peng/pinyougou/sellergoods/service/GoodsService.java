package com.peng.pinyougou.sellergoods.service;
import java.util.List;

import com.peng.pinyougou.pojo.TbGoods;
import com.peng.pinyougou.pojo.TbItem;
import com.peng.pinyougou.pojogroup.Goods;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	public void update(Goods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum,int pageSize);
	
	
	/** 
	 * @param @param id 
	 * @return void  
	 * @Description 审核商品
	 * @author 彭坤
	 * @date 2018年9月2日 下午6:37:50
	 */
	public void updateStatus(Long[] ids,String status);
	
	/** 
	 * @param @param goodsIds
	 * @param @param status
	 * @param @return 
	 * @return List<TbItem>  
	 * @Description 根据spu的id集合查询sku列表
	 * @author 彭坤
	 * @date 2018年9月9日 下午8:41:09
	 */
	public List<TbItem> findItemListByGoodsIdListAndStatus(Long[] goodsIds,String status);
	
}
