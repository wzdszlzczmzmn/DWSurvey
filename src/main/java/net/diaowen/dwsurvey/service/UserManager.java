package net.diaowen.dwsurvey.service;

import net.diaowen.common.base.entity.User;
import net.diaowen.common.plugs.httpclient.HttpResult;
import net.diaowen.common.plugs.page.Page;
import net.diaowen.common.service.BaseService;

/**
 * 用户相关业务逻辑的服务组件的接口
 */
public interface UserManager extends BaseService<User, String>{

	/**
	 * 管理员新增用户
	 *
	 * @param entity 新用户
	 * @return 新增的用户
	 */
	public User adminSave(User entity);

	/**
	 * 分页查询用户列表
	 *
	 * @param page 与具体ORM实现无关的分页查询结果封装
	 * @param entity User对象
	 * @return
	 */
	public Page<User> findPage(Page<User> page, User entity);

	/**
	 * 禁用账号
	 *
	 * @param id 被禁用的用户账号ID
	 */
	public void disUser(String id);

	/**
	 * 判断用户的登录名是否唯一
	 *
	 * @param id 用户ID
	 * @param loginName 用户的登录名
	 * @return 若存在同名用户,则返回同名的用户
	 */
	public User findNameUn(String id, String loginName);

	/**
	 * 判断用户的邮箱是否唯一
	 *
	 * @param id 用户ID
	 * @param email 用户的邮箱
	 * @return 若存在相同邮箱用户,返回该用户
	 */
	public User findEmailUn(String id, String email);

	/**
	 * 根据密码编码查询用户
	 *
	 * @param code 密码的编码
	 * @return 符合条件的用户
	 */
	public User findByCode(String code);

	/**
	 * 根据用户的活跃状态码查询用户
	 *
	 * @param code 活跃状态码
	 * @return 符合条件的用户
	 */
	public User findByActivationCode(String code);

	/**
	 * 重置用户组
	 *
	 * @param groupId 用户组ID
	 */
	public void resetUserGroup(String groupId);

	/**
	 * 根据关键词进行分页查找
	 *
	 * @param page 与具体ORM实现无关的分页查询结果封装
	 * @param keyword 关键词
	 * @return 分页查找的结果
	 */
	public Page<User> findPageByKeyword(Page<User> page, String keyword);

	/**
	 * 分页查找用户
	 *
	 * @param page 与具体ORM实现无关的分页查询结果封装
	 * @param status 用户状态码
	 * @param loginName 用户的登录名
	 * @param name 名字
	 * @param email 邮箱
	 * @param cellphone 电话号码
	 * @return 分页查询的结果
	 */
	public Page<User> findPage(Page<User> page, Integer status, String loginName, String name, String email,String cellphone);

	/**
	 * 用于更新用户信息的方法
	 *
	 * @param user 新的用户信息
	 * @return 更新的结果
	 */
	public HttpResult upData(User user);

	/**
	 * 批量删除用户数据
	 *
	 * @param ids 用户ID字符串数组
	 */
	public void deleteData(String[] ids);
}
