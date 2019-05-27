package entity;

import java.io.Serializable;

/**   
* 项目名称：pinyougou-pojo   
* 类名称：Result   
* 类描述：   统一返回
* 创建人：彭坤   
* 创建时间：2018年8月23日 下午9:35:41      
* @version     
*/
public class Result implements Serializable{

	private boolean success;//是否成功
	private String msg;//消息
	public Result() {
		super();
	}
	public Result(boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
