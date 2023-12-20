package net.diaowen.common.base.dao;

import net.diaowen.common.base.entity.User;
import net.diaowen.common.dao.BaseDao;
import net.diaowen.common.plugs.page.Page;


/**
 * 用户数据访问对象接口，继承自BaseDao。
 *
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface UserDao extends BaseDao<User, String> {
	/**
	 * 重置用户组。
	 *
	 * @param groupId 用户组ID
	 */
	public void resetUserGroup(String groupId);

}
