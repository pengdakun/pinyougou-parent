package com.peng.pinyougou.manager.controller;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**   
* 项目名称：pinyougou-manager-web   
* 类名称：LoginController   
* 类描述：   登录相关controller
* 创建人：彭坤   
* 创建时间：2018年8月26日 下午3:38:17      
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
	 * @date 2018年8月26日 下午3:40:58
	 */
	@RequestMapping(value="/name")
	public Map name(){
		//获得当前登录名称
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map hashMap = new HashMap<>();
		hashMap.put("loginName", name);
		return hashMap;
	}
	
}
