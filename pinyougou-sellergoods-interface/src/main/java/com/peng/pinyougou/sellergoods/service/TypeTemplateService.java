package com.peng.pinyougou.sellergoods.service;
import java.util.List;
import java.util.Map;

import com.peng.pinyougou.pojo.TbTypeTemplate;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface TypeTemplateService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbTypeTemplate> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbTypeTemplate typeTemplate);
	
	
	/**
	 * 修改
	 */
	public void update(TbTypeTemplate typeTemplate);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbTypeTemplate findOne(Long id);
	
	
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
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum,int pageSize);
	
	
	/** 
	 * @param @return 
	 * @return List<Map>  
	 * @Description 查询下拉列表
	 * @author 彭坤
	 * @date 2018年8月27日 下午10:09:18
	 */
	public List<Map> selectOptionList();
	
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return List<Map>  
	 * @Description 通过模板id查询规格属性值
	 * @author 彭坤
	 * @date 2018年8月30日 下午9:31:11
	 */
	public List<Map> findSpecList(Long id);
	
	
}
