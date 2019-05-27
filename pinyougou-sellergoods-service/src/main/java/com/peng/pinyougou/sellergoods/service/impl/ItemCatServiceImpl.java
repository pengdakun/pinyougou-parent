package com.peng.pinyougou.sellergoods.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.peng.pinyougou.mapper.TbItemCatMapper;
import com.peng.pinyougou.pojo.TbItemCat;
import com.peng.pinyougou.pojo.TbItemCatExample;
import com.peng.pinyougou.pojo.TbItemCatExample.Criteria;
import com.peng.pinyougou.sellergoods.service.ItemCatService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbItemCat> findAll() {
		return itemCatMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbItemCat> page=   (Page<TbItemCat>) itemCatMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbItemCat itemCat) {
		itemCatMapper.insert(itemCat);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbItemCat itemCat){
		itemCatMapper.updateByPrimaryKey(itemCat);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbItemCat findOne(Long id){
		return itemCatMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//不存在下一级时删除
			List<TbItemCat> findByParentId = findByParentId(id);
			if (findByParentId==null || findByParentId.size()<=0) {
				itemCatMapper.deleteByPrimaryKey(id);
			}
		}		
	}
	
	
		@Override
	public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		
		if(itemCat!=null){			
						if(itemCat.getName()!=null && itemCat.getName().length()>0){
				criteria.andNameLike("%"+itemCat.getName()+"%");
			}
	
		}
		
		Page<TbItemCat> page= (Page<TbItemCat>)itemCatMapper.selectByExample(example);	
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public List<TbItemCat> findByParentId(Long parentId) {
			TbItemCatExample example=new TbItemCatExample();
			Criteria createCriteria = example.createCriteria();
			createCriteria.andParentIdEqualTo(parentId);
			List<TbItemCat> selectByExample = itemCatMapper.selectByExample(example);
			
			//将模板id加入缓存  一分类名称为key  模板id为value
			List<TbItemCat> findAll = findAll();
			for (TbItemCat tbItemCat : findAll) {
				redisTemplate.boundHashOps("itemCat").put(tbItemCat.getName(), tbItemCat.getTypeId());
			}
			
			return selectByExample;
		}
	
}
