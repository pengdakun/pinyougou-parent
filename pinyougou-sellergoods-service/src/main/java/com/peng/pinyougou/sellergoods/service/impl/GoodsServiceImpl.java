package com.peng.pinyougou.sellergoods.service.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysql.fabric.xmlrpc.base.Array;
import com.peng.pinyougou.mapper.TbBrandMapper;
import com.peng.pinyougou.mapper.TbGoodsDescMapper;
import com.peng.pinyougou.mapper.TbGoodsMapper;
import com.peng.pinyougou.mapper.TbItemCatMapper;
import com.peng.pinyougou.mapper.TbItemMapper;
import com.peng.pinyougou.mapper.TbSellerMapper;
import com.peng.pinyougou.pojo.TbBrand;
import com.peng.pinyougou.pojo.TbGoods;
import com.peng.pinyougou.pojo.TbGoodsDesc;
import com.peng.pinyougou.pojo.TbGoodsDescExample;
import com.peng.pinyougou.pojo.TbGoodsExample;
import com.peng.pinyougou.pojo.TbGoodsExample.Criteria;
import com.peng.pinyougou.pojo.TbItem;
import com.peng.pinyougou.pojo.TbItemCat;
import com.peng.pinyougou.pojo.TbItemExample;
import com.peng.pinyougou.pojo.TbSeller;
import com.peng.pinyougou.pojogroup.Goods;
import com.peng.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {


	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper tbGoodsDescMapper;
	
	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Autowired
	private TbBrandMapper tbBrandMapper;
	
	@Autowired
	private TbSellerMapper tbSellerMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		TbGoods tbGoods = goods.getTbGoods();
		tbGoods.setAuditStatus("0");//状态为未审核
		goodsMapper.insert(tbGoods);
		TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
		tbGoodsDesc.setGoodsId(tbGoods.getId());
		tbGoodsDescMapper.insert(tbGoodsDesc);
		List<TbItem> tbItems = goods.getTbItems();
		saveTbItem(tbGoods, tbGoodsDesc, tbItems);
	}
	
	/** 
	 * @param @param tbItem
	 * @param @param tbGoods
	 * @param @param tbGoodsDesc 
	 * @return void  
	 * @Description 设置SKU属性值
	 * @author 彭坤
	 * @date 2018年9月2日 下午3:32:32
	 */
	private void setItemValues(TbItem tbItem,TbGoods tbGoods,TbGoodsDesc tbGoodsDesc){
		tbItem.setCreateTime(new Date());
		tbItem.setUpdateTime(new Date());
		//商品分类,三级分类
		tbItem.setCategoryid(tbGoods.getCategory3Id());
		tbItem.setGoodsId(tbGoods.getId());
		tbItem.setSellerId(tbGoods.getSellerId());
		
		//设置分类名称
		TbItemCat selectByPrimaryKey = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
		tbItem.setCategory(selectByPrimaryKey.getName());
		
		//设置品牌name
		TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(tbGoods.getBrandId());
		tbItem.setBrand(tbBrand.getName());
		
		//设置商家名称
		TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
		tbItem.setSeller(tbSeller.getNickName());
		
		//图片
		List<Map> parseArray = JSON.parseArray(tbGoodsDesc.getItemImages(),Map.class);
		if (parseArray!=null && parseArray.size()>0) {
			tbItem.setImage(parseArray.get(0).get("url").toString());
		}
	}
	
	/** 
	 * @param @param tbGoods
	 * @param @param tbGoodsDesc
	 * @param @param tbItems 
	 * @return void  
	 * @Description 插入SKU数据
	 * @author 彭坤
	 * @date 2018年9月2日 下午3:32:17
	 */
	private void saveTbItem(TbGoods tbGoods,TbGoodsDesc tbGoodsDesc,List<TbItem> tbItems){
		if ("1".equals(tbGoods.getIsEnableSpec())) {//启用规格
			for (TbItem tbItem : tbItems) {
				//添加标题
				Map<String,Object> map = JSON.parseObject(tbItem.getSpec(),Map.class);
				String title=tbGoods.getGoodsName();
				for(String key:map.keySet()){
					title+=" "+map.get(key);
				}
				tbItem.setTitle(title);
				setItemValues(tbItem, tbGoods, tbGoodsDesc);
				
				tbItemMapper.insert(tbItem);
			}
		}else{//未启用
			TbItem tbItem=new TbItem();
			tbItem.setSpec("{}");
			tbItem.setTitle(tbGoods.getGoodsName());
			tbItem.setPrice(tbGoods.getPrice());
			tbItem.setNum(99999);
			tbItem.setStatus("1");
			tbItem.setIsDefault("1");
			setItemValues(tbItem, tbGoods, tbGoodsDesc);
			tbItemMapper.insert(tbItem);
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		//更新基本表数据
		TbGoods tbGoods = goods.getTbGoods();
		goodsMapper.updateByPrimaryKey(tbGoods);
		TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
		tbGoodsDescMapper.updateByPrimaryKey(tbGoodsDesc);
		//先删除SKU
		TbItemExample example=new TbItemExample();
		com.peng.pinyougou.pojo.TbItemExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andGoodsIdEqualTo(tbGoods.getId());
		tbItemMapper.deleteByExample(example);
		//添加SKU
		saveTbItem(tbGoods, tbGoodsDesc, goods.getTbItems());
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setTbGoods(tbGoods);
		TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(id);
		goods.setTbGoodsDesc(tbGoodsDesc);
		TbItemExample example=new TbItemExample();
		com.peng.pinyougou.pojo.TbItemExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andGoodsIdEqualTo(id);
		List<TbItem> selectByExample = tbItemMapper.selectByExample(example);
		goods.setTbItems(selectByExample);
		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsDelete("1");//表示逻辑删除
			goodsMapper.updateByPrimaryKey(tbGoods);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeleteIsNull();//筛选掉删除的商品
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
//				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public void updateStatus(Long[] ids,String status) {
			for (Long long1 : ids) {
				TbGoods tbGoods = goodsMapper.selectByPrimaryKey(long1);
				tbGoods.setAuditStatus(status);
				goodsMapper.updateByPrimaryKey(tbGoods);
			}
		}
		
		public List<TbItem> findItemListByGoodsIdListAndStatus(Long[] goodsIds,String status){
			TbItemExample example=new TbItemExample();
			com.peng.pinyougou.pojo.TbItemExample.Criteria createCriteria = example.createCriteria();
			createCriteria.andStatusEqualTo(status);
			createCriteria.andGoodsIdIn(Arrays.asList(goodsIds));
			return tbItemMapper.selectByExample(example);
		}
	
}
