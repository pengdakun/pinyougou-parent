package com.peng.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.peng.pinyougou.pojo.TbBrand;

import entity.PageResult;

/**   
* 项目名称：pinyougou-sellergoods-interface   
* 类名称：BrandService   
* 类描述：   品牌管理
* 创建人：彭坤   
* 创建时间：2018年8月22日 下午8:27:33      
* @version     
*/
public interface BrandService {
	
	/** 
	 * @param @return 
	 * @return List<TbBrand>  
	 * @Description 查询所有品牌
	 * @author 彭坤
	 * @date 2018年8月22日 下午8:28:28
	 */
	public List<TbBrand> getAll();
	
	/** 
	 * @param @param pageNum
	 * @param @param pageSize
	 * @param @param tbBrand
	 * @param @return 
	 * @return PageResult  
	 * @Description 根据条件执行分页查询
	 * @author 彭坤
	 * @date 2018年8月23日 下午8:07:57
	 */
	public PageResult findPage(int pageNum,int pageSize,TbBrand tbBrand);
	
	
	/** 
	 * @param @param tbBrand 
	 * @return void  
	 * @Description 添加
	 * @author 彭坤
	 * @date 2018年8月23日 下午9:33:28
	 */
	public void add(TbBrand tbBrand);
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return TbBrand  
	 * @Description 通过id查询
	 * @author 彭坤
	 * @date 2018年8月23日 下午9:46:21
	 */
	public TbBrand findOne(Long id);
	
	/** 
	 * @param @param tbBrand 
	 * @return void  
	 * @Description 修改
	 * @author 彭坤
	 * @date 2018年8月23日 下午9:46:57
	 */
	public void update(TbBrand tbBrand);
	
	
	/** 
	 * @param @param ids 
	 * @return void  
	 * @Description 删除
	 * @author 彭坤
	 * @date 2018年8月23日 下午9:57:36
	 */
	public void delete(Long[] ids);
	
	/** 
	 * @param @return 
	 * @return List<Map>  
	 * @Description 查询下拉列表使用
	 * @author 彭坤
	 * @date 2018年8月25日 下午3:34:28
	 */
	public List<Map> selectOptionList();
	
}
