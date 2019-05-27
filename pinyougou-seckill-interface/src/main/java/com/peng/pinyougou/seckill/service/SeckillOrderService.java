package com.peng.pinyougou.seckill.service;
import java.util.List;

import com.peng.pinyougou.pojo.TbSeckillOrder;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SeckillOrderService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSeckillOrder> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbSeckillOrder seckillOrder);
	
	
	/**
	 * 修改
	 */
	public void update(TbSeckillOrder seckillOrder);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbSeckillOrder findOne(Long id);
	
	
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
	public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum,int pageSize);
	
	
	/** 
	 * @param @param secKillGoodsId 
	 * @return void  
	 * @Description 秒杀下单
	 * @author 彭坤
	 * @date 2018年10月2日 下午1:41:44
	 */
	public void submitSeckillOrder(Long secKillGoodsId,String userId);
	
	
	/** 
	 * @param @param userId
	 * @param @return 
	 * @return List<TbSeckillOrder>  
	 * @Description 从redis中提取订单
	 * @author 彭坤
	 * @date 2018年10月2日 下午2:39:49
	 */
	public TbSeckillOrder searchOrderFromRedisByUserId(String userId);
	
	
	/** 
	 * @param @param userId
	 * @param @param orderId 
	 * @return void  
	 * @Description 保存秒杀订单到数据库
	 * @author 彭坤
	 * @date 2018年10月2日 下午2:53:32
	 */
	public void saveSecKillOrder(String userId,Long orderId,String transactionId);
	
	
	/** 
	 * @param @param userId
	 * @param @param orderId 
	 * @return void  
	 * @Description 删除缓存中的订单
	 * @author 彭坤
	 * @date 2018年10月2日 下午3:24:52
	 */
	public void deleteOrderFromRedis(String userId,Long orderId);
	
}
