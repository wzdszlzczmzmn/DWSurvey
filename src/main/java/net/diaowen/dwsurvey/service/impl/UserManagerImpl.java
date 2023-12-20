package net.diaowen.dwsurvey.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.diaowen.common.plugs.httpclient.HttpResult;
import net.diaowen.dwsurvey.service.UserManager;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.diaowen.common.base.dao.UserDao;
import net.diaowen.common.base.entity.User;
import net.diaowen.common.plugs.page.Page;
import net.diaowen.common.service.BaseServiceImpl;
import net.diaowen.common.utils.security.DigestUtils;


/**
 * 用户管理业务逻辑相关的服务组件
 *
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class UserManagerImpl extends BaseServiceImpl<User, String> implements UserManager {

	/**
	 * 用户数据的数据访问接口
	 */
	@Autowired
	private UserDao userDao;

	/**
	 * 设置BaseDao对象
	 */
	@Override
	public void setBaseDao() {
		this.baseDao=userDao;
	}

	/**
	 * 管理员新增用户
	 *
	 * @param entity 新用户
	 * @return 新增的用户
	 */
	@Override
	public User adminSave(User entity) {
		if(entity!=null){
			if(StringUtils.isNotEmpty(entity.getId())){
				entity.setId(null);
			}
			String pwd=entity.getPwd();
			if(pwd!=null && !"".equals(pwd)){
				String shaPassword = DigestUtils.sha1Hex(pwd);
				entity.setShaPassword(shaPassword);
			}
			super.save(entity);
			return entity;
		}
		return null;
	}

	/**
	 * 分页查询用户列表
	 *
	 * @param page 与具体ORM实现无关的分页查询结果封装
	 * @param entity User对象
	 * @return
	 */
	public Page<User> findPage(Page<User> page, User entity) {
		List<Criterion> criterions=new ArrayList<>();
		Integer status = entity.getStatus();
		String loginName = entity.getLoginName();
		if(status != null){
			criterions.add(Restrictions.eq("status", status));
		}
		if(loginName!=null && !"".equals(loginName)){
			criterions.add(Restrictions.like("loginName", "%"+loginName+"%"));
		}
		criterions.add(Restrictions.disjunction().add(Restrictions.eq("visibility", 1)).add(Restrictions.isNull("visibility")));
		page.setOrderBy("createTime");
		page.setOrderDir("desc");
		return super.findPageByCri(page, criterions);
	}

	/**
	 * 禁用账号
	 *
	 * @param id 被禁用的用户账号ID
	 */
	@Transactional
	@Override
	public void disUser(String id) {
		User user=get(id);
		int status=user.getStatus();
		if(status==0){
			user.setStatus(1);
		}else{
			user.setStatus(0);
		}
		save(user);
	}

	/**
	 * 删除账号
	 *
	 * @param id 被删除的用户实体ID
	 */
	@Transactional
	@Override
	public void delete(String id) {
		User user=get(id);
		user.setVisibility(0);
		save(user);
	}

	/**
	 * 判断用户的登录名是否唯一
	 *
	 * @param id 用户ID
	 * @param loginName 用户的登录名
	 * @return 若存在同名用户,则返回同名的用户
	 */
	@Override
	public User findNameUn(String id, String loginName) {
		List<Criterion> criterions=new ArrayList<>();
		criterions.add(Restrictions.eq("loginName", loginName));
		if(id!=null && !"".equals(id)){
			criterions.add(Restrictions.ne("id", id));
		}
		return userDao.findFirst(criterions);
	}

	/**
	 * 判断用户的邮箱是否唯一
	 *
	 * @param id 用户ID
	 * @param email 用户的邮箱
	 * @return 若存在相同邮箱用户,返回该用户
	 */
	@Override
	public User findEmailUn(String id, String email) {
		List<Criterion> criterions=new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("email", email));
		if(id!=null && !"".equals(id)){
			criterions.add(Restrictions.ne("id", id));
		}
		return userDao.findFirst(criterions);
	}

	/**
	 * 根据密码编码查询用户
	 *
	 * @param code 密码的编码
	 * @return 符合条件的用户
	 */
	@Override
	public User findByCode(String code) {
		Criterion criterion=Restrictions.eq("findPwdCode", code);
		return userDao.findFirst(criterion);
	}

	/**
	 * 根据用户的活跃状态码查询用户
	 *
	 * @param activationCode 活跃状态码
	 * @return 符合条件的用户
	 */
	@Override
	public User findByActivationCode(String activationCode) {
		Criterion criterion=Restrictions.eq("activationCode", activationCode);
		return userDao.findFirst(criterion);
	}

	/**
	 * 重置用户组
	 *
	 * @param groupId 用户组ID
	 */
	@Override
	public void resetUserGroup(String groupId) {
		userDao.resetUserGroup(groupId);
	}

	/**
	 * 根据关键词进行分页查找
	 *
	 * @param page 与具体ORM实现无关的分页查询结果封装
	 * @param keyword 关键词
	 * @return 分页查找的结果
	 */
	@Override
	public Page<User> findPageByKeyword(Page<User> page, String keyword) {
		Criterion cri1=Restrictions.like("name", "%"+keyword+"%");
		return userDao.findPage(page,cri1);
	}

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
	public Page<User> findPage(Page<User> page, Integer status, String loginName, String name, String email,String cellphone) {
		List<Criterion> criterions=new ArrayList<Criterion>();
		if(status!=null){
			criterions.add(Restrictions.eq("status", status));
		}
		if(StringUtils.isNotEmpty(loginName)){
			criterions.add(Restrictions.like("loginName", "%"+loginName+"%"));
		}
		if(StringUtils.isNotEmpty(name)){
			criterions.add(Restrictions.like("name", "%"+name+"%"));
		}
		if(StringUtils.isNotEmpty(email)){
			criterions.add(Restrictions.like("email", "%"+email+"%"));
		}
		if(StringUtils.isNotEmpty(cellphone)){
			criterions.add(Restrictions.like("cellphone", "%"+cellphone+"%"));
		}
		criterions.add(Restrictions.disjunction().add(Restrictions.eq("visibility", 1)).add(Restrictions.isNull("visibility")));
		if(StringUtils.isEmpty(page.getOrderBy())){
			page.setOrderBy("createTime");
			page.setOrderDir("desc");
		}
		return super.findPageByCri(page, criterions);
	}

	/**
	 * 用于更新用户信息的方法
	 *
	 * @param user 新的用户信息
	 * @return 更新的结果
	 */
	@Transactional
	@Override
	public HttpResult upData(User user) {
		if(user!=null){
			String id = user.getId();
			if(id!=null){
				User dbUser = getModel(id);
				dbUser.setLoginName(user.getLoginName());
				dbUser.setStatus(user.getStatus());
				String pwd = user.getPwd();
				if(StringUtils.isNotEmpty(pwd)) {
					//加点盐
					String shaPassword = DigestUtils.sha1Hex(pwd);
					dbUser.setShaPassword(shaPassword);
				}
				super.save(dbUser);
				return HttpResult.SUCCESS();
			}
		}
		return HttpResult.FAILURE("user为null");
	}

	/**
	 * 批量删除用户数据
	 *
	 * @param ids 用户ID字符串数组
	 */
	@Transactional
	@Override
	public void deleteData(String[] ids) {
		for (String id:ids) {
			userDao.delete(id);
		}
	}

}
