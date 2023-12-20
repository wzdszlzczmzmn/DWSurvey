package net.diaowen.common.base.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


import net.diaowen.common.plugs.mapper.CollectionMapper;

/**
 *	用户实体类
 *
 * @author KeYuan
 * @date 2013下午10:02:00
 *user实体类，继承自IdEntity
 */
@Entity
@Table(name = "t_user")
public class User extends IdEntity {
	private String loginName;
	private String shaPassword;
	//用户名
	private String name;//
	//邮箱
	private String email;
	//出生年月
	private Date birthday;
	//最高学历
	private Integer eduQuali;
	//性别
	private Integer sex;
	//
	private String avatar;

	//2激活 1未激活 0不可用
	private Integer status=1;// 账号状态
	private Integer version = 1;//1 默认 2测试
	private Date createTime = new Date();
	private String createBy = "";
	private Date lastLoginTime;
	private String cellphone;

	//激活账号CODE
	private String activationCode;
	//找回密码code   ""或null表示没有激活找回密码功能
	private String findPwdCode;
	//找回密码最后期限  默认设置一天之内
	private Date findPwdLastDate;

	//加点盐
	private String salt;
	// 是否显示 0不显示
	private Integer visibility = 1;

	private String shaPasswordTemp;

	private String wxOpenId;
	private String sessionId;

	// Hibernate自动维护的Version字段
	// @Version
	/**
	 * 获取版本号。
	 *
	 * @return 版本号
	 */
	public Integer getVersion() {
		return version;
	}
	/**
	 * 设置版本号。
	 *
	 * @param version 版本号
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
	/**
	 * 获取登录用户名。
	 *
	 * @return 登录用户名
	 */
	@Column(nullable = false, unique = true)
	public String getLoginName() {
		return loginName;
	}
	/**
	 * 设置版本号。
	 *
	 * @param loginName 登录用户名
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * 获取密码。
	 *
	 * @return 密码
	 */
	public String getShaPassword() {
		return shaPassword;
	}
	/**
	 * 设置密码
	 *
	 * @param shaPassword 密码
	 */
	public void setShaPassword(String shaPassword) {
		this.shaPassword = shaPassword;
	}
	/**
	 * 获取用户名字。
	 *
	 * @return 用户名字
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置用户名字。
	 *
	 * @param name 用户名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取用户邮箱。
	 *
	 * @return 用户邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置用户邮箱
	 *
	 * @param email 用户邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取用户账号状态。
	 *
	 * @return status 用户账号状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置用户邮箱
	 *
	 * @param status 用户账号状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取用户创建时间。
	 *
	 * @return createTime 用户账号创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置用户创建时间
	 *
	 * @param createTime 用户账号创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取用户创建人。
	 *
	 * @return createBy 用户创建人
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * 设置用户创建人
	 *
	 * @param createBy 用户创建人
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取手机号码
	 *
	 * @return 手机号码
	 */
	public String getCellphone() {
		return cellphone;
	}
	/**
	 * 设置手机号码
	 *
	 * @param cellphone 手机号码
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	/**
	 * 获取生日
	 *
	 * @return 生日
	 */
	public Date getBirthday() {
		return birthday;
	}
	/**
	 * 设置生日
	 *
	 * @param birthday 生日
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	/**
	 * 获取教育程度
	 *
	 * @return 教育程度
	 */
	public Integer getEduQuali() {
		return eduQuali;
	}
	/**
	 * 设置教育程度
	 *
	 * @param eduQuali 教育程度
	 */
	public void setEduQuali(Integer eduQuali) {
		this.eduQuali = eduQuali;
	}
	/**
	 * 获取性别
	 *
	 * @return 性别
	 */
	public Integer getSex() {
		return sex;
	}
	/**
	 * 设置性别
	 *
	 * @param sex 性别
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	/**
	 * 获取最后登录时间
	 *
	 * @return 最后登录时间
	 */
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	/**
	 * 设置最后登录时间
	 *
	 * @param lastLoginTime 最后登录时间
	 */
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	/**
	 * 获取激活码
	 *
	 * @return 激活码
	 */
	public String getActivationCode() {
		return activationCode;
	}
	/**
	 * 设置激活码
	 *
	 * @param activationCode 激活码
	 */
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	/**
	 * 获取盐值
	 *
	 * @return 盐值
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * 设置盐值
	 *
	 * @param salt 盐值
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	/**
	 * 获取找回密码功能码
	 *
	 * @return 找回密码功能码
	 */
	public String getFindPwdCode() {
		return findPwdCode;
	}
	/**
	 * 设置找回密码功能码
	 *
	 * @param findPwdCode 找回密码功能码
	 */
	public void setFindPwdCode(String findPwdCode) {
		this.findPwdCode = findPwdCode;
	}
	/**
	 * 获取找回密码最后时间
	 *
	 * @return 找回密码最后时间
	 */
	public Date getFindPwdLastDate() {
		return findPwdLastDate;
	}
	/**
	 * 设置找回密码最后时间
	 *
	 * @param findPwdLastDate 找回密码最后时间
	 */
	public void setFindPwdLastDate(Date findPwdLastDate) {
		this.findPwdLastDate = findPwdLastDate;
	}
	/**
	 * 获取可见性
	 *
	 * @return 可见性
	 */
	public Integer getVisibility() {
		return visibility;
	}
	/**
	 * 设置可见性
	 *
	 * @param visibility 可见性
	 */
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	/**
	 * 获取头像
	 *
	 * @return 头像
	 */
	public String getAvatar() {
		return avatar;
	}
	/**
	 * 设置头像
	 *
	 * @param avatar 头像
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * 获取sha加密密码的转换值
	 * @return ShaPasswordTemp
	 */
	public String getShaPasswordTemp() {
		return shaPasswordTemp;
	}
	/**
	 * 设置sha加密密码的转换值
	 *
	 * @param shaPasswordTemp 转换值
	 */
	public void setShaPasswordTemp(String shaPasswordTemp) {
		this.shaPasswordTemp = shaPasswordTemp;
	}

	/**
	 * 获取微信用户标识名
	 * @return 微信用户标识名
	 */
	public String getWxOpenId() {
		return wxOpenId;
	}
	/**
	 * 设置微信用户标识名
	 *
	 * @param wxOpenId 用户标识名
	 */
	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	/**
	 * 获取sessionId
	 * @return SessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * 设置sessionId
	 * @param sessionId sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	private String plainPassword;

	/**
	 * 获取未加密的密码
	 * @return plainPassword
	 */
	@Transient
	public String getPlainPassword() {
		return plainPassword;
	}

	/**
	 * 设置未加密的密码
	 * @param plainPassword 未加密密码
	 */
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	private String pwd;

	/**
	 * 获取最终的密码
	 * @return 密码
	 */
	@Transient
	public String getPwd() {
		return pwd;
	}

	/**
	 * 设置最终的密码
	 * @param pwd 密码
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	private String findPwdUrl="";

	/**
	 * 获取找回密码的URL
	 * @return URL
	 */
	@Transient
	public String getFindPwdUrl() {
		return findPwdUrl;
	}

	/**
	 * 设置找回密码的URL
	 * @param findPwdUrl URL
	 */
	public void setFindPwdUrl(String findPwdUrl) {
		this.findPwdUrl = findPwdUrl;
	}

	private String wwwooo;

	public String getWwwooo() {
		return wwwooo;
	}

	public void setWwwooo(String wwwooo) {
		this.wwwooo = wwwooo;
	}


}
