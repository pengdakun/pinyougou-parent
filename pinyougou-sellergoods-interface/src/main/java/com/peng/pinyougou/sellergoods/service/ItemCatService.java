package com.peng.pinyougou.sellergoods.service;
import java.util.List;

import com.peng.pinyougou.pojo.TbItemCat;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface ItemCatService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbItemCat> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbItemCat itemCat);
	
	
	/**
	 * 修改
	 */
	public void update(TbItemCat itemCat);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbItemCat findOne(Long id);
	
	
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
	public PageResult findPage(TbItemCat itemCat, int pageNum,int pageSize);
	
	
	/** 
	 * @param @param parentId
	 * @param @return 
	 * @return List<TbItemCat>  
	 * @Description 通过父id查询
	 * @author 彭坤
	 * @date 2018年8月27日 下午9:42:24
	 */
	public List<TbItemCat> findByParentId(Long parentId);
	
}
