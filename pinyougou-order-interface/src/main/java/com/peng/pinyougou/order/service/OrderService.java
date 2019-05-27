package com.peng.pinyougou.order.service;
import java.util.List;

import com.peng.pinyougou.pojo.TbOrder;
import com.peng.pinyougou.pojo.TbPayLog;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbOrder> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrder order);
	
	
	/**
	 * 修改
	 */
	public void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbOrder findOne(Long id);
	
	
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
	public PageResult findPage(TbOrder order, int pageNum,int pageSize);
	
	/** 
	 * @param @param userId
	 * @param @return 
	 * @return TbPayLog  
	 * @Description 通过用户id获得支付日志
	 * @author 彭坤
	 * @date 2018年10月1日 下午2:31:38
	 */
	public TbPayLog searchPayLogFromRedis(String userId);
	
	
	/** 
	 * @param @param out_trade_no
	 * @param @param transaction_id 
	 * @return void  
	 * @Description 支付成功修改订单状态
	 * @author 彭坤
	 * @date 2018年10月1日 下午2:47:39
	 */
	public void updateOrderStatus(String out_trade_no,String transaction_id);
	
}
