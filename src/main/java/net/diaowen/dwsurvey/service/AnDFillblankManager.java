package net.diaowen.dwsurvey.service;

import java.util.List;

import net.diaowen.common.plugs.page.Page;
import net.diaowen.common.service.BaseService;
import net.diaowen.dwsurvey.entity.AnDFillblank;
import net.diaowen.dwsurvey.entity.Question;

/**
 * 多项填空题业务 接口
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface AnDFillblankManager extends BaseService<AnDFillblank, String>{
	/**
	 * 根据exam_user信息查询答案
	 *
	 * @param belongAnswerId 所属答案的ID
	 * @param quId 题目ID
	 * @return 返回多项填空题的答案列表
	 */
	public List<AnDFillblank> findAnswer(String belongAnswerId, String quId);

	/**
	 * 统计多项填空题的每个填空项的数量
	 *
	 * @param question 问题对象，用于获取相关属性值
	 */
	public void findGroupStats(Question question);

	/**
	 * 分页查询
	 *
	 * @param anPage 分页对象，用于定义分页属性（页码、每页大小等）
	 * @param quItemId 填空项ID
	 * @return 分页的答案列表
	 */
	Page<AnDFillblank> findPage(Page<AnDFillblank> anPage, String quItemId);
}
