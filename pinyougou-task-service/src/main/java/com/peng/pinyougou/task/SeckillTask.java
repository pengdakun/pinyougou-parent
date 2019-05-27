package com.peng.pinyougou.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.peng.pinyougou.mapper.TbSeckillGoodsMapper;
import com.peng.pinyougou.pojo.TbSeckillGoods;
import com.peng.pinyougou.pojo.TbSeckillGoodsExample;
import com.peng.pinyougou.pojo.TbSeckillGoodsExample.Criteria;

/**   
* 项目名称：pinyougou-task-service   
* 类名称：SeckillTask   
* 类描述：   秒杀任务
* 创建人：彭坤   
* 创建时间：2018年10月2日 下午4:10:53      
* @version     
*/
@Component
public class SeckillTask {
	
	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/** 
	 * @param  
	 * @return void  
	 * @Description 定时添加秒杀商品到redis
	 * @author 彭坤
	 * @date 2018年10月2日 下午5:52:06
	 */
	@Scheduled(cron="0 * * * * ?")
	public void refreshSeckillGoods(){
		
		//先查询缓存商品的id集合
		Set keys=redisTemplate.boundHashOps("secKillGoods").keys();
		TbSeckillGoodsExample example=new TbSeckillGoodsExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andStatusEqualTo("1");//审核通过
		createCriteria.andStockCountGreaterThan(0);//数量大于0
		createCriteria.andStartTimeLessThanOrEqualTo(new Date());//开始时间小于等于当前时间
		createCriteria.andEndTimeGreaterThanOrEqualTo(new Date());//结束日期大于等于当前日期
		
		if (keys!=null && keys.size()>0) {
			createCriteria.andIdNotIn(new ArrayList(keys));//查询id不在缓存中的
		}
		
		List<TbSeckillGoods> values = seckillGoodsMapper.selectByExample(example);
		for (TbSeckillGoods tbSeckillGoods : values) {//将数据加入缓存
			redisTemplate.boundHashOps("secKillGoods").put(tbSeckillGoods.getId(), tbSeckillGoods);
			System.out.println("增量更新商品id="+tbSeckillGoods.getId());
		}
	}
	
	/** 
	 * @param  
	 * @return void  
	 * @Description 定时移除redis中的过期秒杀商品
	 * @author 彭坤
	 * @date 2018年10月2日 下午5:52:25
	 */
	@Scheduled(cron="* * * * * ?")
	public void removeSecKillGoodsFromRedis(){
		List<TbSeckillGoods> list= redisTemplate.boundHashOps("secKillGoods").values();
		for (TbSeckillGoods tbSeckillGoods : list) {
			if (tbSeckillGoods.getEndTime().getTime()<new Date().getTime()) {
				seckillGoodsMapper.updateByPrimaryKey(tbSeckillGoods);//同步数据库
				redisTemplate.boundHashOps("secKillGoods").delete(tbSeckillGoods.getId());//移除缓存
				System.out.println("移除缓存秒杀商品id="+tbSeckillGoods.getId());
			}
		}
	}

}
