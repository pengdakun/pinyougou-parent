package com.peng.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.peng.pinyougou.pojo.TbOrderItem;

/**   
* 项目名称：pinyougou-pojo   
* 类名称：Cart   
* 类描述：   页面显示购物车列表
* 创建人：彭坤   
* 创建时间：2018年9月23日 下午3:54:20      
* @version     
*/
public class Cart implements Serializable{
	
	private String sellerId;//商家id
	private String sellerName;//商家名称
	private List<TbOrderItem> orderItemList;//购物车明细
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public List<TbOrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<TbOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
}
