package net.diaowen.dwsurvey.service;

import java.util.List;

import net.diaowen.common.service.BaseService;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.entity.AnOrder;

/**
 * 排序题业务 接口
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface AnOrderManager extends BaseService<AnOrder, String>{

	/**
	 * 根据exam_user信息查找AnOrder列表
	 *
	 * @param belongAnswerId 所属答案的ID
	 * @param quId 问题的ID
	 * @return AnOrder列表
	 */
	public List<AnOrder>  findAnswer(String belongAnswerId, String quId);

	/**
	 * 统计一个排序问题中的每个选项对应的排序序号
	 * @param question 问题对象
	 **/
	public void findGroupStats(Question question);
}
