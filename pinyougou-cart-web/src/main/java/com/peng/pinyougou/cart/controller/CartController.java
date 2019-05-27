package com.peng.pinyougou.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.peng.pinyougou.cart.service.CartService;
import com.peng.pinyougou.pojogroup.Cart;
import com.peng.pinyougou.util.CookieUtil;

import entity.Result;

/**   
* 项目名称：pinyougou-cart-web   
* 类名称：CartController   
* 类描述：   购物车controller
* 创建人：彭坤   
* 创建时间：2018年9月23日 下午4:57:53      
* @version     
*/
@RestController
@RequestMapping("/cart")
public class CartController {
	
	
	@Reference
	private CartService cartService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	/** 
	 * @param @param itemId
	 * @param @param num
	 * @param @return 
	 * @return Result  
	 * @Description 添加商品到购物车
	 * @author 彭坤
	 * @date 2018年9月23日 下午4:59:06
	 */
	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins="http://localhost:9105")//解决跨域
	public Result addGoodsToCartList(Long itemId,Integer num){
		try {
			//设置跨域请求,*表示所有请求都可已访问
//			response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
			//当此方法不需要操作cookie时，上面一句话即可，否则加下面这句话,如果要操作cookie，http://localhost:9105不能为*
//			response.setHeader("Access-Control-Allow-Credentials", "true");//允许使用时cookie
			
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			List<Cart> cartList = findCartListByCookie();
			//添加购物车
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
			if ("anonymousUser".equals(name)) {//用户未登录
				//将新的购物车存入cookie
				CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 24*60*60, "UTF-8");
			}else{
				cartService.saveCartListToRedis(name, cartList);
			}
			return new Result(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加成功失败");
		}
	}
	
	
	/** 
	 * @param @param request
	 * @param @return 
	 * @return List<Cart>  
	 * @Description 查询cookie购物车
	 * @author 彭坤
	 * @date 2018年9月23日 下午5:02:12
	 */
	@RequestMapping("/findCartList")
	public List<Cart> findCartListByCookie(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		
		//获得redis购物车
		String cookieValue = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if(cookieValue==null || "".equals(cookieValue)){
			cookieValue="[]";
		}
		List<Cart> cartList_cookie = JSON.parseArray(cookieValue, Cart.class);
		
		if ("anonymousUser".equals(name)) {//用户未登录
			return cartList_cookie;
		}else{
			List<Cart> cartList_redis = cartService.findCartListFromRedis(name);
			if (cartList_cookie!=null && cartList_cookie.size()>0) {
				//合并购物车
				List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
				cartService.saveCartListToRedis(name, cartList);
				//清除cookie购物车
				CookieUtil.deleteCookie(request, response, "cartList");
				return cartList;
			}
			return cartList_redis;
		}
	};
	
}
