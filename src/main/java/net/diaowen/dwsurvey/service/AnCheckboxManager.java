package net.diaowen.dwsurvey.service;

import java.util.List;

import net.diaowen.common.service.BaseService;
import net.diaowen.dwsurvey.entity.AnCheckbox;
import net.diaowen.dwsurvey.entity.DataCross;
import net.diaowen.dwsurvey.entity.Question;

/**
 * 多选题业务 接口
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */

public interface AnCheckboxManager extends BaseService<AnCheckbox, String>{

	/**
	 * 根据exam_user信息查询答案
	 *
	 * @param belongAnswerId 所属问卷的ID
	 * @param quId 问题的ID
	 * @return 返回多选题答案列表
	 */
	public List<AnCheckbox> findAnswer(String belongAnswerId, String quId);

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
