package net.diaowen.dwsurvey.dao;

import net.diaowen.common.dao.BaseDao;
import net.diaowen.dwsurvey.entity.AnDFillblank;
import net.diaowen.dwsurvey.entity.Question;

/**
 * 多项填空DAO interface
 * @author KeYuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */
public interface AnDFillblankDao extends BaseDao<AnDFillblank, String>{

	/**
	 * 统计多项填空题的每个填空项的数量
	 *
	 * @param question 问题对象，用于获取相关属性值
	 */
	public void findGroupStats(Question question);

}
