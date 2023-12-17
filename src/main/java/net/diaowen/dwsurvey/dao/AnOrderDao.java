package net.diaowen.dwsurvey.dao;

import net.diaowen.common.dao.BaseDao;
import net.diaowen.dwsurvey.entity.AnOrder;
import net.diaowen.dwsurvey.entity.Question;

/**
 * 排序题DAO interface
 * @author KeYuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */
public interface AnOrderDao extends BaseDao<AnOrder, String>{

	/**
	 * 统计一个排序问题中的每个选项对应的排序序号
	 * @param question 问题对象
	 **/
	public void findGroupStats(Question question);

}
