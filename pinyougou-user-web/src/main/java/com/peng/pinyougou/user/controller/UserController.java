package com.peng.pinyougou.user.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.peng.pinyougou.pojo.TbUser;
import com.peng.pinyougou.user.service.UserService;
import com.peng.pinyougou.util.PhoneFormatCheckUtils;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String smsCode){
		try {
			boolean checkSmsCode = userService.checkSmsCode(user.getPhone(), smsCode);
			if (!checkSmsCode) {
				return new Result(false,"验证码不正确");
			}
			userService.add(user);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		try {
			userService.update(user);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbUser findOne(Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}
	
	/** 
	 * @param @param phone
	 * @param @return 
	 * @return Result  
	 * @Description 发送验证码
	 * @author 彭坤
	 * @date 2018年9月22日 下午1:17:29
	 */
	@RequestMapping("/sendCode")
	public Result sendCode(String phone){
		try {
			//校验号码
			boolean phoneLegal = PhoneFormatCheckUtils.isPhoneLegal(phone);
			if (phoneLegal) {
				userService.createSmsCode(phone);
				return new Result(true, "发送成功");
			}
			return new Result(false, "号码格式不正确!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(true, "发送失败");
		}
	}
	
}
