package com.peng.pinyougou.cart.service;

import java.util.List;

import com.peng.pinyougou.pojogroup.Cart;

/**   
* 项目名称：pinyougou-cart-interface   
* 类名称：CartService   
* 类描述：   购物车service
* 创建人：彭坤   
* 创建时间：2018年9月23日 下午3:57:26      
* @version     
*/
public interface CartService {
	
	
	/** 
	 * @param @param cartList
	 * @param @param itemId
	 * @param @param num
	 * @param @return 
	 * @return List<Cart>  
	 * @Description 添加商品到购物车列表
	 * @author 彭坤
	 * @date 2018年9月23日 下午4:00:11
	 */
	public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);
	
	
	/** 
	 * @param @param userName
	 * @param @return 
	 * @return List<Cart>  
	 * @Description 从redis取购物车
	 * @author 彭坤
	 * @date 2018年9月24日 下午12:54:08
	 */
	public List<Cart> findCartListFromRedis(String userName);
	
	/** 
	 * @param @param userName
	 * @param @param cartList 
	 * @return void  
	 * @Description 将购物车列表存入redis
	 * @author 彭坤
	 * @date 2018年9月24日 下午12:55:05
	 */
	public void saveCartListToRedis(String userName,List<Cart> cartList);
	
	/** 
	 * @param @param cookieCartList
	 * @param @param redisCartList
	 * @param @return 
	 * @return List<Cart>  
	 * @Description 合并购物车
	 * @author 彭坤
	 * @date 2018年9月24日 下午2:44:29
	 */
	public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);

}
