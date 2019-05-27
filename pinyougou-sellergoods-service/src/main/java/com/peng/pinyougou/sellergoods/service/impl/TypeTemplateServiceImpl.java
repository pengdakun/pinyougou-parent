package com.peng.pinyougou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.peng.pinyougou.mapper.TbSpecificationOptionMapper;
import com.peng.pinyougou.mapper.TbTypeTemplateMapper;
import com.peng.pinyougou.pojo.TbSpecificationOption;
import com.peng.pinyougou.pojo.TbSpecificationOptionExample;
import com.peng.pinyougou.pojo.TbTypeTemplate;
import com.peng.pinyougou.pojo.TbTypeTemplateExample;
import com.peng.pinyougou.pojo.TbTypeTemplateExample.Criteria;
import com.peng.pinyougou.sellergoods.service.TypeTemplateService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;
	
	@Autowired
	private TbSpecificationOptionMapper tbSpecificationOptionMapper;
	
	@Autowired
	private  RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbTypeTemplate> page=   (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate){
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id){
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			typeTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbTypeTemplateExample example=new TbTypeTemplateExample();
		Criteria criteria = example.createCriteria();
		
		if(typeTemplate!=null){			
						if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
			if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
				criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}
			if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
				criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}
			if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}
	
		}
		Page<TbTypeTemplate> page= (Page<TbTypeTemplate>)typeTemplateMapper.selectByExample(example);
		
		saveRedis();
		return new PageResult(page.getTotal(), page.getResult());
	}
		
		/** 
		 * @param  
		 * @return void  
		 * @Description 将品牌列表与规格列表放入缓存
		 * @author 彭坤
		 * @date 2018年9月8日 下午1:34:21
		 */
		private void saveRedis(){
			List<TbTypeTemplate> findAll = findAll();
			for (TbTypeTemplate tbTypeTemplate : findAll) {
				//缓存品牌列表
				List<Map> brandList = JSON.parseArray(tbTypeTemplate.getBrandIds(),Map.class);
				redisTemplate.boundHashOps("barndList").put(tbTypeTemplate.getId(), brandList);
				//缓存规格列表
				List<Map> specList = findSpecList(tbTypeTemplate.getId());
				redisTemplate.boundHashOps("specList").put(tbTypeTemplate.getId(), specList);
			}
			
		}

		@Override
		public List<Map> selectOptionList() {
			return typeTemplateMapper.selectOptionList();
		}

		@Override
		public List<Map> findSpecList(Long id) {
			TbTypeTemplate selectByPrimaryKey = typeTemplateMapper.selectByPrimaryKey(id);
			List<Map> parseArray = JSON.parseArray(selectByPrimaryKey.getSpecIds(), Map.class);
			for (Map map : parseArray) {
				TbSpecificationOptionExample example=new TbSpecificationOptionExample();
				com.peng.pinyougou.pojo.TbSpecificationOptionExample.Criteria createCriteria = example.createCriteria();
				createCriteria.andSpecIdEqualTo(new Long((Integer)map.get("id")));
				List<TbSpecificationOption> selectByExample = tbSpecificationOptionMapper.selectByExample(example);
				map.put("options", selectByExample);
			}
			return parseArray;
		}
	
}
