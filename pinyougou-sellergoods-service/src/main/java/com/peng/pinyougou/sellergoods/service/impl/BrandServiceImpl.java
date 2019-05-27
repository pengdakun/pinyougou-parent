package com.peng.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.peng.pinyougou.mapper.TbBrandMapper;
import com.peng.pinyougou.pojo.TbBrand;
import com.peng.pinyougou.pojo.TbBrandExample;
import com.peng.pinyougou.pojo.TbBrandExample.Criteria;
import com.peng.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

	@Autowired
	private TbBrandMapper tbBrandMapper;
	
	@Override
	public List<TbBrand> getAll() {
		return tbBrandMapper.selectByExample(null);
	}
	
	@Override
	public PageResult findPage(int pageNum, int pageSize, TbBrand tbBrand) {
		
		PageHelper.startPage(pageNum, pageSize);
		//添加查询条件
		TbBrandExample tbBrandExample = new TbBrandExample();
		Criteria criteria = tbBrandExample.createCriteria();
		if(tbBrand!=null){
			if(tbBrand.getName()!=null && !"".equals(tbBrand.getName())){
				criteria.andNameLike("%"+tbBrand.getName()+"%");
			}
			if(tbBrand.getFirstChar()!=null && !"".equals(tbBrand.getFirstChar())){
				criteria.andFirstCharLike("%"+tbBrand.getFirstChar()+"%");
			}
		}
		Page<TbBrand> selectByExample = (Page<TbBrand>) tbBrandMapper.selectByExample(tbBrandExample);
		return new PageResult(selectByExample.getTotal(), selectByExample.getResult());
	}

	@Override
	public void add(TbBrand tbBrand) {
		tbBrandMapper.insert(tbBrand);
	}

	@Override
	public TbBrand findOne(Long id) {
		return tbBrandMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(TbBrand tbBrand) {
		tbBrandMapper.updateByPrimaryKey(tbBrand);
	}

	@Override
	public void delete(Long[] ids) {
		for (Long long1 : ids) {
			tbBrandMapper.deleteByPrimaryKey(long1);
		}
	}

	@Override
	public List<Map> selectOptionList() {
		return tbBrandMapper.selectOptionList();
	}



}
