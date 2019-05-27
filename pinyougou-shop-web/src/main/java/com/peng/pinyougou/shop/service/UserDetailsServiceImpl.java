package com.peng.pinyougou.shop.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.peng.pinyougou.pojo.TbSeller;
import com.peng.pinyougou.sellergoods.service.SellerService;

/**   
* 项目名称：pinyougou-shop-web   
* 类名称：UserDetailsServiceImpl   
* 类描述：   用户登录认证类，可以从数据库中查询
* 创建人：彭坤   
* 创建时间：2018年8月27日 下午8:34:59      
* @version     
*/
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private SellerService sellerService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//构建角色
		List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		//查询用户
		TbSeller tbSeller = sellerService.findOne(username);
		if (tbSeller!=null) {
			if (tbSeller.getStatus().equals("1")) {
				return new User(username, tbSeller.getPassword(), authorities);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	public SellerService getSellerService() {
		return sellerService;
	}
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	
	

}
