package com.peng.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.peng.pinyougou.pojo.TbGoods;
import com.peng.pinyougou.pojo.TbGoodsDesc;
import com.peng.pinyougou.pojo.TbItem;

/**   
* 项目名称：pinyougou-pojo   
* 类名称：Goods   
* 类描述：   商品组合实体类
* 创建人：彭坤   
* 创建时间：2018年8月28日 下午8:45:56      
* @version     
*/
public class Goods implements Serializable{
	
	private TbGoods tbGoods;
	private TbGoodsDesc tbGoodsDesc;
	private List<TbItem> tbItems;//sku列表
	public TbGoods getTbGoods() {
		return tbGoods;
	}
	public void setTbGoods(TbGoods tbGoods) {
		this.tbGoods = tbGoods;
	}
	public TbGoodsDesc getTbGoodsDesc() {
		return tbGoodsDesc;
	}
	public void setTbGoodsDesc(TbGoodsDesc tbGoodsDesc) {
		this.tbGoodsDesc = tbGoodsDesc;
	}
	public List<TbItem> getTbItems() {
		return tbItems;
	}
	public void setTbItems(List<TbItem> tbItems) {
		this.tbItems = tbItems;
	}
}
