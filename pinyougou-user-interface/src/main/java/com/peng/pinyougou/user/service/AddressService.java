package com.peng.pinyougou.user.service;
import java.util.List;

import com.peng.pinyougou.pojo.TbAddress;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface AddressService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbAddress> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbAddress address);
	
	
	/**
	 * 修改
	 */
	public void update(TbAddress address);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbAddress findOne(Long id);
	
	
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
	public PageResult findPage(TbAddress address, int pageNum,int pageSize);
	
	
	/** 
	 * @param @param userId
	 * @param @return 
	 * @return List<TbAddress>  
	 * @Description 通过用户名查询用户收货地址 
	 * @author 彭坤
	 * @date 2018年9月25日 下午9:01:51
	 */
	public List<TbAddress> findListByUserId(String userId);
	
}
