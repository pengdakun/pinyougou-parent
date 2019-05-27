package com.peng.pinyougou.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.peng.pinyougou.cart.service.CartService;
import com.peng.pinyougou.mapper.TbItemMapper;
import com.peng.pinyougou.pojo.TbItem;
import com.peng.pinyougou.pojo.TbOrderItem;
import com.peng.pinyougou.pojogroup.Cart;

/**   
* 项目名称：pinyougou-cart-service   
* 类名称：CartServiceImpl   
* 类描述：   购物车实现
* 创建人：彭坤   
* 创建时间：2018年9月23日 下午4:00:59      
* @version     
*/
@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		//通过skuid查询商品明细的对象
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		if (tbItem==null) {
			throw new RuntimeException("商品不存在");
		}
		if (!tbItem.getStatus().equals("1")) {
			throw new RuntimeException("商品状态不合法");
		}
		//根据sku对象得到商家id
		String sellerId = tbItem.getSellerId();
		//根据商家id在购物车列表中查找购物车对象
		if (cartList==null || cartList.size()<=0) {
			cartList=new ArrayList<Cart>();
		}
		Cart cart = searchCartBySellerId(cartList, sellerId);
		if (cart==null) {
			//如果购物车在列表中不存在,创建一个新的购物车对象，将新的购物车对象添加到购物车列表
			cart=new Cart();
			cart.setSellerId(sellerId);
			cart.setSellerName(tbItem.getSeller());
			List<TbOrderItem> list = new ArrayList<TbOrderItem>();
			TbOrderItem tbOrderItem = createOrderItem(tbItem, num);
			list.add(tbOrderItem);
			cart.setOrderItemList(list);
			cartList.add(cart);
		}else{
			//如果购物车列表中存在该商家购物车,判断商品是否在该购物车列表中是否存在
			List<TbOrderItem> tbOrderItems = cart.getOrderItemList();
			TbOrderItem tbOrderItem = searchOrderItemByItemId(tbOrderItems, itemId);
			if (tbOrderItem==null) {
				//商品不存在，则创建新的，添加到该商家得购物车中
				tbOrderItem=createOrderItem(tbItem, num);
				cart.getOrderItemList().add(tbOrderItem);
			}else{
				//商品存在则数量相加,更新金额
				tbOrderItem.setNum(tbOrderItem.getNum()+num);
				tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue()*tbOrderItem.getNum()));
				if (tbOrderItem.getNum()<=0) {//当数量小于0，移除
					cart.getOrderItemList().remove(tbOrderItem);
				}
				if (cart.getOrderItemList().size()<=0) {//当商家得购物车不存在了，移除
					cartList.remove(cart);
				}
			}
		}
		return cartList;
	}
	
	/** 
	 * @param @param cartList
	 * @param @param sellerId
	 * @param @return 
	 * @return Cart  
	 * @Description 查询购物车列表中是否存在某商家得列表
	 * @author 彭坤
	 * @date 2018年9月23日 下午4:17:08
	 */
	private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
		for (Cart cart : cartList) {
			if (cart.getSellerId().equals(sellerId)) {
				return cart;
			}
		}
		return null;
	}
	
	/** 
	 * @param @param tbItem
	 * @param @param num
	 * @param @return 
	 * @return TbOrderItem  
	 * @Description 创建购物车详情对象
	 * @author 彭坤
	 * @date 2018年9月23日 下午4:27:05
	 */
	private TbOrderItem createOrderItem(TbItem tbItem,Integer num){
		TbOrderItem tbOrderItem = new TbOrderItem();
		tbOrderItem.setGoodsId(tbItem.getGoodsId());
		tbOrderItem.setItemId(tbItem.getId());
		tbOrderItem.setNum(num);
		tbOrderItem.setPicPath(tbItem.getImage());
		tbOrderItem.setPrice(tbItem.getPrice());
		tbOrderItem.setSellerId(tbItem.getSellerId());
		tbOrderItem.setTitle(tbItem.getTitle());
		tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue()*num));
		return tbOrderItem;
	}
	
	
	/** 
	 * @param @param tbOrderItems
	 * @param @param itemId
	 * @param @return 
	 * @return TbOrderItem  
	 * @Description 查询商家购物车列表中是否存在某商品
	 * @author 彭坤
	 * @date 2018年9月23日 下午4:43:23
	 */
	public TbOrderItem searchOrderItemByItemId(List<TbOrderItem> tbOrderItems,Long itemId){
		for (TbOrderItem tbOrderItem : tbOrderItems) {
			if (tbOrderItem.getItemId().longValue()==itemId.longValue()) {
				return tbOrderItem;
			}
		}
		return null;
	}

	@Override
	public List<Cart> findCartListFromRedis(String userName) {
		List<Cart> cartList=(List<Cart>) redisTemplate.boundHashOps("cartList").get(userName);
		if (cartList==null) {
			cartList=new ArrayList<Cart>();
		}
		return cartList;
	}

	@Override
	public void saveCartListToRedis(String userName, List<Cart> cartList) {
		redisTemplate.boundHashOps("cartList").put(userName,cartList);
	}

	@Override
	public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
		for (Cart cart : cartList2) {
			for (TbOrderItem tbOrderItem : cart.getOrderItemList()) {
				cartList1 = addGoodsToCartList(cartList1, tbOrderItem.getItemId(), tbOrderItem.getNum());
			}
		}
		return cartList1;
	}

}
