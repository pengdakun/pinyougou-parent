package com.peng.pinyougou.portal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.peng.pinyougou.content.service.ContentService;
import com.peng.pinyougou.pojo.TbContent;

/**   
* 项目名称：pinyougou-portal-web   
* 类名称：ContentController   
* 类描述：   广告controller
* 创建人：彭坤   
* 创建时间：2018年9月3日 下午8:34:49      
* @version     
*/
@RestController
@RequestMapping(value="/content")
public class ContentController {
	
	@Reference
	private ContentService contentService;
	
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return List<TbContent>  
	 * @Description 通过广告类目查询广告
	 * @author 彭坤
	 * @date 2018年9月3日 下午8:36:00
	 */
	@RequestMapping(value="/findByCategoryId")
	public List<TbContent> findByCategoryId(Long id) {
		return contentService.findByCategoryId(id);
	}

}
