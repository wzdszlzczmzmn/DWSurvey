package net.diaowen.common.base.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author KeYuan
 * @date 2013下午10:01:45
 *跟踪器实体类，继承自IdEntity
 */
@Entity
@Table(name = "tracker")
public class Tracker extends IdEntity {

	private String dataType;
	private String dataId;
	private String operation;
	private Date optime = new Date();
	private String loginName;
	/**
	 * 无参构造函数。
	 */
	public Tracker() {
		super();
	}
	/**
	 * 构造函数。
	 *
	 * @param dataType 数据类型
	 * @param dataId 数据ID
	 * @param operation 操作
	 * @param loginName 登录名
	 */
	public Tracker(String dataType, String dataId, String operation, String loginName) {
		super();
		this.dataType = dataType;
		this.dataId = dataId;
		this.operation = operation;
		this.loginName = loginName;
	}
	/**
	 * 获取数据类型。
	 *
	 * @return 数据类型
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * 设置数据类型。
	 *
	 * @param dataType 数据类型
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * 获取数据ID。
	 *
	 * @return 数据ID
	 */
	public String getDataId() {
		return dataId;
	}
	/**
	 * 设置数据ID。
	 *
	 * @param dataId 数据ID
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	/**
	 * 获取操作。
	 *
	 * @return 操作
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * 设置操作。
	 *
	 * @param operation 操作
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	/**
	 * 获取操作时间。
	 *
	 * @return 操作时间
	 */
	public Date getOptime() {
		return optime;
	}
	/**
	 * 设置操作时间。
	 *
	 * @param optime 操作时间
	 */
	public void setOptime(Date optime) {
		this.optime = optime;
	}
	/**
	 * 获取登录名。
	 *
	 * @return 登录名
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * 设置登录名。
	 *
	 * @param loginName 登录名
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}
