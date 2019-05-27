package com.peng.pinyougou.seckill.service.impl;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.peng.pinyougou.mapper.TbSeckillGoodsMapper;
import com.peng.pinyougou.mapper.TbSeckillOrderMapper;
import com.peng.pinyougou.pojo.TbSeckillGoods;
import com.peng.pinyougou.pojo.TbSeckillOrder;
import com.peng.pinyougou.pojo.TbSeckillOrderExample;
import com.peng.pinyougou.pojo.TbSeckillOrderExample.Criteria;
import com.peng.pinyougou.seckill.service.SeckillOrderService;
import com.peng.pinyougou.util.IdWorker;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

	@Autowired
	private TbSeckillOrderMapper seckillOrderMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private TbSeckillGoodsMapper tbSeckillGoodsMapper;
	
	@Autowired
	private IdWorker idWorker;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillOrder> findAll() {
		return seckillOrderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSeckillOrder> page=   (Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillOrder seckillOrder) {
		seckillOrderMapper.insert(seckillOrder);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillOrder seckillOrder){
		seckillOrderMapper.updateByPrimaryKey(seckillOrder);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillOrder findOne(Long id){
		return seckillOrderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			seckillOrderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSeckillOrderExample example=new TbSeckillOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(seckillOrder!=null){			
						if(seckillOrder.getUserId()!=null && seckillOrder.getUserId().length()>0){
				criteria.andUserIdLike("%"+seckillOrder.getUserId()+"%");
			}
			if(seckillOrder.getSellerId()!=null && seckillOrder.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+seckillOrder.getSellerId()+"%");
			}
			if(seckillOrder.getStatus()!=null && seckillOrder.getStatus().length()>0){
				criteria.andStatusLike("%"+seckillOrder.getStatus()+"%");
			}
			if(seckillOrder.getReceiverAddress()!=null && seckillOrder.getReceiverAddress().length()>0){
				criteria.andReceiverAddressLike("%"+seckillOrder.getReceiverAddress()+"%");
			}
			if(seckillOrder.getReceiverMobile()!=null && seckillOrder.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+seckillOrder.getReceiverMobile()+"%");
			}
			if(seckillOrder.getReceiver()!=null && seckillOrder.getReceiver().length()>0){
				criteria.andReceiverLike("%"+seckillOrder.getReceiver()+"%");
			}
			if(seckillOrder.getTransactionId()!=null && seckillOrder.getTransactionId().length()>0){
				criteria.andTransactionIdLike("%"+seckillOrder.getTransactionId()+"%");
			}
	
		}
		
		Page<TbSeckillOrder> page= (Page<TbSeckillOrder>)seckillOrderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public void submitSeckillOrder(Long secKillGoodsId, String userId) {
			//通过秒杀商品id从redis中查询数据
			TbSeckillGoods tbSeckillGoods=(TbSeckillGoods) redisTemplate.boundHashOps("secKillGoods").get(secKillGoodsId);
			if (tbSeckillGoods==null) {
				throw new RuntimeException("秒杀商品不存在!");
			}
			if (tbSeckillGoods.getStockCount()<=0) {
				throw new RuntimeException("秒杀商品已经被抢光啦!");
			}
			//减数量
			tbSeckillGoods.setStockCount(tbSeckillGoods.getStockCount()-1);
			redisTemplate.boundHashOps("secKillGoods").put(secKillGoodsId, tbSeckillGoods);
			if (tbSeckillGoods.getStockCount()<=0) {//商品已经被秒杀完
				tbSeckillGoodsMapper.updateByPrimaryKey(tbSeckillGoods);//更新数据库
				redisTemplate.boundHashOps("secKillGoods").delete(secKillGoodsId);
				System.out.println("将商品同步到数据库！！！！！！！！！！！");
			}
			//添加订单(不向数据库存,存入缓存，支付完毕存入数据库)
			TbSeckillOrder tbSeckillOrder = new TbSeckillOrder();
			tbSeckillOrder.setId(idWorker.nextId());
			tbSeckillOrder.setSeckillId(secKillGoodsId);
			tbSeckillOrder.setMoney(tbSeckillGoods.getCostPrice());
			tbSeckillOrder.setUserId(userId);
			tbSeckillOrder.setSellerId(tbSeckillGoods.getSellerId());
			tbSeckillOrder.setCreateTime(new Date());
			tbSeckillOrder.setStatus("0");
			redisTemplate.boundHashOps("secKillOrder").put(userId, tbSeckillOrder);//存入秒杀订单
			System.out.println("将订单保存到redis！！！！！！！！！！！");
		}

		@Override
		public TbSeckillOrder searchOrderFromRedisByUserId(String userId) {
			return (TbSeckillOrder) redisTemplate.boundHashOps("secKillOrder").get(userId);
		}

		@Override
		public void saveSecKillOrder(String userId,Long orderId,String transactionId) {
			TbSeckillOrder tbSeckillOrder=searchOrderFromRedisByUserId(userId);
			if (tbSeckillOrder==null) {
				throw new RuntimeException("不存在该订单");
			}
			if (orderId.longValue()!=tbSeckillOrder.getId().longValue()) {
				throw new RuntimeException("查询的订单数据不一致");
			}
			//修改订单实体属性
			tbSeckillOrder.setTransactionId(transactionId);
			tbSeckillOrder.setPayTime(new Date());
			tbSeckillOrder.setStatus("1");
			//插入数据库
			seckillOrderMapper.insert(tbSeckillOrder);
			//清徐缓存中订单
			redisTemplate.boundHashOps("secKillOrder").delete(userId);
		}

		@Override
		public void deleteOrderFromRedis(String userId, Long orderId) {
			//先查询缓存订单
			TbSeckillOrder tbSeckillOrder=searchOrderFromRedisByUserId(userId);
			if(tbSeckillOrder!=null){
				//删除缓存
				redisTemplate.boundHashOps("secKillOrder").delete(userId);
				//回退库存
				TbSeckillGoods tbSeckillGoods=(TbSeckillGoods) redisTemplate.boundHashOps("secKillGoods").get(tbSeckillOrder.getSeckillId());
				if(tbSeckillGoods!=null){//缓存中存在商品
					tbSeckillGoods.setStockCount(tbSeckillGoods.getStockCount()+1);
				}else{//缓存中不存在商品，从redis中查询
					tbSeckillGoods = tbSeckillGoodsMapper.selectByPrimaryKey(tbSeckillOrder.getSeckillId());
					tbSeckillGoods.setStockCount(1);
				}
				redisTemplate.boundHashOps("secKillGoods").put(tbSeckillGoods.getId(), tbSeckillGoods);
			}
		}
	
}
