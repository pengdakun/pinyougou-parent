package com.peng.pinyougou.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.peng.pinyougou.pojo.TbBrand;
import com.peng.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

/**   
* 项目名称：pinyougou-manager-web   
* 类名称：BrandController   
* 类描述：   品牌管理controller
* 创建人：彭坤   
* 创建时间：2018年8月22日 下午8:30:40      
* @version     
*/
@RestController
@RequestMapping(value="/brand")
public class BrandController {
	
	@Reference
	private BrandService brandService;
	
	@RequestMapping(value="/getAll")
	public List<TbBrand> findAll(){
		return brandService.getAll();
	}
	
	/** 
	 * @param @param page
	 * @param @param size
	 * @param @param tbBrand
	 * @param @return 
	 * @return PageResult  
	 * @Description 通过查询条件进行分页查询
	 * @author 彭坤
	 * @date 2018年8月23日 下午8:14:23
	 */
	@RequestMapping(value="/search")
	public PageResult search(int page,int rows,@RequestBody TbBrand tbBrand){
		return brandService.findPage(page, rows, tbBrand);
	}
	
	/** 
	 * @param @param tbBrand
	 * @param @return 
	 * @return Result  
	 * @Description 添加品牌
	 * @author 彭坤
	 * @date 2018年8月23日 下午9:36:11
	 */
	@RequestMapping(value="/add")
	public Result add(@RequestBody TbBrand tbBrand){
		try {
			brandService.add(tbBrand);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加失败");
		}
	}
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return TbBrand  
	 * @Description 通过id查询
	 * @author 彭坤
	 * @date 2018年8月23日 下午9:48:17
	 */
	@RequestMapping(value="/findOne")
	public TbBrand findOne(Long id){
		return brandService.findOne(id);
	}
	
	/** 
	 * @param @param tbBrand
	 * @param @return 
	 * @return Result  
	 * @Description 修改
	 * @author 彭坤
	 * @date 2018年8月23日 下午9:55:53
	 */
	@RequestMapping(value="/update")
	public Result update(@RequestBody TbBrand tbBrand){
		try {
			brandService.update(tbBrand);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}
	
	/** 
	 * @param @param ids
	 * @param @return 
	 * @return Result  
	 * @Description 批量删除
	 * @author 彭坤
	 * @date 2018年8月23日 下午9:58:26
	 */
	@RequestMapping(value="/delete")
	public Result delete(Long[] ids){
		try {
			brandService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	/** 
	 * @param @return 
	 * @return List<Map>  
	 * @Description 查询下拉列表使用
	 * @author 彭坤
	 * @date 2018年8月25日 下午3:36:32
	 */
	@RequestMapping(value="/selectOoptionList")
	public List<Map> selectOoptionList(){
		return brandService.selectOptionList();
	}	

}
