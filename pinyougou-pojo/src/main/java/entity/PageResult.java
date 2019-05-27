package entity;

import java.io.Serializable;
import java.util.List;

/**   
* 项目名称：pinyougou-pojo   
* 类名称：PageResult   
* 类描述：   分页数据包装
* 创建人：彭坤   
* 创建时间：2018年8月23日 下午8:06:08      
* @version     
*/
public class PageResult implements Serializable {
	
	private Long total;//总条数
	private List rows;//每页数据
	public PageResult() {
		super();
	}
	public PageResult(Long total, List rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}

}
