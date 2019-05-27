package com.peng.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.peng.pinyougou.pojo.TbSpecification;
import com.peng.pinyougou.pojo.TbSpecificationOption;

/**   
* 项目名称：pinyougou-pojo   
* 类名称：Specification   
* 类描述：   包装类，接收页面数据
* 创建人：彭坤   
* 创建时间：2018年8月25日 下午1:48:07      
* @version     
*/
public class Specification implements Serializable {
	
	private TbSpecification specification;
	private List<TbSpecificationOption> specificationOptionList;
	
	public TbSpecification getSpecification() {
		return specification;
	}
	public void setSpecification(TbSpecification specification) {
		this.specification = specification;
	}
	public List<TbSpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}
	public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}

}
