package com.peng.pinyougou.seckill.service;
import java.util.List;

import com.peng.pinyougou.pojo.TbSeckillGoods;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SeckillGoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSeckillGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbSeckillGoods seckillGoods);
	
	
	/**
	 * 修改
	 */
	public void update(TbSeckillGoods seckillGoods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbSeckillGoods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbSeckillGoods seckillGoods, int pageNum,int pageSize);
	
	
	/** 
	 * @param @return 
	 * @return List<TbSeckillGoods>  
	 * @Description 返回正在秒杀的商品
	 * @author 彭坤
	 * @date 2018年10月1日 下午4:45:18
	 */
	public List<TbSeckillGoods> findList();
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return TbSeckillGoods  
	 * @Description 从redis中读取
	 * @author 彭坤
	 * @date 2018年10月1日 下午7:06:18
	 */
	public TbSeckillGoods findOneFromRedis(Long id);
	
}
