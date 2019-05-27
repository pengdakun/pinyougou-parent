package com.peng.pinyougou.content.service;
import java.util.List;

import javax.swing.text.AbstractDocument.Content;

import com.peng.pinyougou.pojo.TbContent;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface ContentService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbContent> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbContent content);
	
	
	/**
	 * 修改
	 */
	public void update(TbContent content);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbContent findOne(Long id);
	
	
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
	public PageResult findPage(TbContent content, int pageNum,int pageSize);
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return List<TbContent>  
	 * @Description 通过广告类目查询广告
	 * @author 彭坤
	 * @date 2018年9月3日 下午8:31:55
	 */
	public List<TbContent> findByCategoryId(Long id);
	
}
