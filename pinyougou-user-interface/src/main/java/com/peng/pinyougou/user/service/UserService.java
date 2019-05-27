package com.peng.pinyougou.user.service;
import java.util.List;

import com.peng.pinyougou.pojo.TbUser;

import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface UserService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbUser> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbUser user);
	
	
	/**
	 * 修改
	 */
	public void update(TbUser user);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbUser findOne(Long id);
	
	
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
	public PageResult findPage(TbUser user, int pageNum,int pageSize);
	
	/** 
	 * @param @param phone 
	 * @return void  
	 * @Description 发送验证码
	 * @author 彭坤
	 * @date 2018年9月22日 下午1:07:36
	 */
	public void createSmsCode(String phone);
	
	/** 
	 * @param @param phone
	 * @param @param code
	 * @param @return 
	 * @return boolean  
	 * @Description 校验验证码
	 * @author 彭坤
	 * @date 2018年9月22日 下午1:39:43
	 */
	public boolean checkSmsCode(String phone,String code);
	
}
