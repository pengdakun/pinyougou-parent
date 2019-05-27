package com.peng.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.peng.pinyougou.mapper.TbOrderItemMapper;
import com.peng.pinyougou.mapper.TbOrderMapper;
import com.peng.pinyougou.mapper.TbPayLogMapper;
import com.peng.pinyougou.order.service.OrderService;
import com.peng.pinyougou.pojo.TbOrder;
import com.peng.pinyougou.pojo.TbOrderExample;
import com.peng.pinyougou.pojo.TbOrderExample.Criteria;
import com.peng.pinyougou.pojo.TbOrderItem;
import com.peng.pinyougou.pojo.TbPayLog;
import com.peng.pinyougou.pojogroup.Cart;
import com.peng.pinyougou.util.IdWorker;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	
	@Autowired
	private TbPayLogMapper tbPayLogMapper;
	
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		//创从redis中提取购物车列表
		List<Cart> carts=(List<Cart>)redisTemplate.boundHashOps("cartList").get(order.getUserId());
		//循环购物车列表，添加订单
		List<String> orderList=new ArrayList<String>();
		double totalMoney = 0;
		for (Cart cart : carts) {
			TbOrder tbOrder = new TbOrder();
			long orderId = idWorker.nextId();
			tbOrder.setOrderId(orderId);
			tbOrder.setPaymentType(order.getPaymentType());
			tbOrder.setStatus("1");//未付款
			tbOrder.setCreateTime(new Date());
			tbOrder.setUpdateTime(new Date());
			tbOrder.setUserId(order.getUserId());
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());//收货人地址
			tbOrder.setReceiverMobile(order.getReceiverMobile());//收货电话
			tbOrder.setReceiver(order.getReceiver());//收货人
			tbOrder.setSourceType(order.getSourceType());//定单来源
			tbOrder.setSellerId(cart.getSellerId());//商家id
			List<TbOrderItem> orderItemList = cart.getOrderItemList();
			double money=0;
			for (TbOrderItem tbOrderItem : orderItemList) {
				
				tbOrderItem.setId(idWorker.nextId());
				tbOrderItem.setOrderId(orderId);
				tbOrderItem.setSellerId(cart.getSellerId());
				tbOrderItemMapper.insert(tbOrderItem);
				money+=tbOrderItem.getTotalFee().doubleValue();
			}
			tbOrder.setPayment(new BigDecimal(money));
			orderMapper.insert(tbOrder);
			
			orderList.add(orderId+"");
			totalMoney+=money;
		}
		//添加支付日志记录
		if (order.getPaymentType().equals("1")) {//微信支付时，添加支付日志
			TbPayLog tbPayLog = new TbPayLog();
			tbPayLog.setCreateTime(new Date());
			tbPayLog.setOutTradeNo(idWorker.nextId()+"");
			tbPayLog.setPayType("1");
			tbPayLog.setUserId(order.getUserId());
			tbPayLog.setOrderList(orderList.toString().replaceAll("[","").replaceAll("]", ""));
			tbPayLog.setTotalFee((long)(totalMoney*100));
			tbPayLog.setTradeState("0");
			tbPayLogMapper.insert(tbPayLog);
			
			//添加用户支付记录到缓存
			redisTemplate.boundHashOps("payLog").put(order.getUserId(), tbPayLog);
		}
		
		
		
		//清除redis中的购物车
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());
		
		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public TbPayLog searchPayLogFromRedis(String userId) {
			TbPayLog tbPayLog=(TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
			return tbPayLog;
		}

		@Override
		public void updateOrderStatus(String out_trade_no, String transaction_id) {
			//修改支付日志状态
			TbPayLog tbPayLog = tbPayLogMapper.selectByPrimaryKey(out_trade_no);
			tbPayLog.setTransactionId(transaction_id);//微信交易流水号
			tbPayLog.setPayTime(new Date());
			tbPayLog.setTradeState("1");
			tbPayLogMapper.updateByPrimaryKey(tbPayLog);
			//修改订单表状态
			String orderList = tbPayLog.getOrderList();
			String[] split = orderList.split(",");
			for (String string : split) {
				TbOrder tbOrder = orderMapper.selectByPrimaryKey(Long.parseLong(string));
				tbOrder.setStatus("2");
				tbOrder.setPaymentTime(new Date());
				orderMapper.updateByPrimaryKey(tbOrder);
			}
			//清除缓存中的支付日志
			redisTemplate.boundHashOps("payLog").delete(tbPayLog.getUserId());
		}
		
}
