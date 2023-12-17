package net.diaowen.dwsurvey.dao;

import java.util.List;

import net.diaowen.common.dao.BaseDao;
import net.diaowen.dwsurvey.entity.AnCheckbox;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.entity.DataCross;


/**
 * 多选题DAO interface
 * @author KeYuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */
public interface AnCheckboxDao extends BaseDao<AnCheckbox, String>{

	/**
	 * 计算一个多选问题中的每个选项被选择的次数。
	 * @param question 问题对象
	 **/
	public void findGroupStats(Question question);

	/**
	 * 查找指定两个问题的交叉统计信息
	 * @param rowQuestion 行问题对象，此处为多选题类型
	 * @param colQuestion 列问题对象
	 * @return 包含交叉统计信息的列表
	 */
	public List<DataCross> findStatsDataCross(Question rowQuestion,
											  Question colQuestion);
	/**
	 * 计算一个多选问题中的每个选项被选择的次数，记录在列表中
	 * @param question 问题对象
	 * @return 统计信息列表
	 */
	public List<DataCross> findStatsDataChart(Question question);

}
