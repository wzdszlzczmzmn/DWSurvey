package net.diaowen.common.base.service;

import java.util.Date;
import java.util.List;

import net.diaowen.common.base.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.diaowen.common.base.dao.UserDao;
import net.diaowen.common.exception.ServiceException;
import net.diaowen.common.plugs.security.ShiroDbRealm;
import net.diaowen.common.utils.security.DigestUtils;

/**
 *
 * @author KeYuan
 * @date 2013下午10:22:04
 *用户管理类
 */
@Service
public class AccountManager {

	private static Logger logger = LoggerFactory.getLogger(AccountManager.class);
	private static final String LOGINNAME = "loginName";
	@Autowired
	private UserDao userDao;

	private ShiroDbRealm shiroRealm;

	/**
	 * 在保存用户时,发送用户修改通知消息, 由消息接收者异步进行较为耗时的通知邮件发送.
	 *
	 * 如果企图修改超级用户,取出当前操作员用户,打印其信息然后抛出异常.
	 *
	 */
	// 演示指定非默认名称的TransactionManager.
	@Transactional
	public void saveUser(User user) {
		if (isSupervisor(user)) {
			logger.warn("操作员{}尝试修改超级管理员用户", SecurityUtils.getSubject()
					.getPrincipal());
			throw new ServiceException("不能修改超级管理员用户");
		}
		//判断是否有重复用户
		String shaPassword = DigestUtils.sha1Hex(user.getPlainPassword());
		user.setShaPassword(shaPassword);
		userDao.save(user);
		if (shiroRealm != null) {
			shiroRealm.clearCachedAuthorizationInfo(user.getLoginName());
		}
	}

	/**
	 * 保存用户
	 * @param user user
	 */
	@Transactional
	public void saveUp(User user){
		if (isSupervisor(user)) {
			logger.warn("操作员{}尝试修改超级管理员用户", SecurityUtils.getSubject().getPrincipal());
			throw new ServiceException("不能修改超级管理员用户");
		}
		userDao.save(user);
	}

	/**
	 * 修改密码
	 * @param curpwd 现在的密码
	 * @param newPwd 想要修改的密码
	 * @return 修改成功或失败
	 */
	@Transactional
	public boolean updatePwd(String curpwd, String newPwd) {
		User user = getCurUser();
		if(user!=null&&curpwd!=null && newPwd!=null){
				//判断是否有重复用户
				String curShaPassword = DigestUtils.sha1Hex(curpwd);
				if(user.getShaPassword().equals(curShaPassword)){
					String shaPassword = DigestUtils.sha1Hex(newPwd);
					user.setShaPassword(shaPassword);
					userDao.save(user);
					return true;
				}
		}
		return false;
	}


	/**
	 * 判断是否超级管理员.
	 *
	 * @param user 用户
	 * @return 真假
	 */
	private boolean isSupervisor(User user) {

		return (user.getId() != null && user.getId() == "1");
	}

	/**
	 * 通过ID获取用户
	 * @param id Userid
	 * @return 用户
	 */
	@Transactional(readOnly = true)
	public User getUser(String id) {
		return userDao.get(id);
	}

	/**
	 * 通过LoginName获取用户
	 * @param loginName 用户登录名
	 * @return 用户
	 */
	@Transactional(readOnly = true)
	public User findUserByLoginName(String loginName) {
		return userDao.findUniqueBy(LOGINNAME, loginName);
	}

	/**
	 * 通过email或loginName获取用户
	 * @param loginName email或loginName
	 * @return 用户
	 */
	@Transactional(readOnly = true)
	public User findUserByLoginNameOrEmail(String loginName) {
		User user = null;
		if(loginName!=null){
			user = userDao.findUniqueBy(LOGINNAME, loginName);
			if(user==null && loginName.contains("@")){
				//是邮箱账号
				user = userDao.findUniqueBy("email", loginName);
			}
		}
		return user;
	}

	/**
	 * 通过邮箱获取用户
	 * @param email 邮箱
	 * @return 用户
	 */
	/*验证邮箱是否存在*/
	@Transactional(readOnly = true)
	public User findUserByEmail(String email){
		List<User> users=userDao.findBy("email", email);
		if(users!=null && users.size()>0){
			return users.get(0);
		}
		return null;
	}

	/**
	 * 检查用户名是否唯一.
	 *
	 * @return loginName在数据库中唯一或等于oldLoginName时返回true.
	 */
	@Transactional(readOnly = true)
	public boolean isLoginNameUnique(String newLoginName, String oldLoginName) {
		return userDao.isPropertyUnique(LOGINNAME, newLoginName, oldLoginName);
	}

	/**
	 * 取出当前登陆用户
	 *
	 * @return 用户
	 */
	public User getCurUser(){
		Subject subject=SecurityUtils.getSubject();

		if(subject!=null){
			Object principal=subject.getPrincipal();
			if(principal!=null){
				User user = findUserByLoginName(principal.toString());
				return user;
			}
		}
		return null;
	}



}
