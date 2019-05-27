package com.peng.pinyougou.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**   
* 项目名称：pinyougou-shop-web   
* 类名称：LoginController   
* 类描述：   登录controller
* 创建人：彭坤   
* 创建时间：2018年8月27日 下午9:10:48      
* @version     
*/
@RestController
@RequestMapping(value="/login")
public class LoginController {

	/** 
	 * @param @return 
	 * @return Map  
	 * @Description 获得登录用户名
	 * @author 彭坤
	 * @date 2018年8月27日 下午9:10:58
	 */
	@RequestMapping(value="/name")
	public Map name(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap<>();
		map.put("loginName", name);
		return map;
	}
	
}
