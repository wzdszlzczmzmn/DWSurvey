package net.diaowen.dwsurvey.service;

import net.diaowen.common.plugs.page.Page;
import net.diaowen.common.service.BaseService;
import net.diaowen.dwsurvey.entity.AnFillblank;
import net.diaowen.dwsurvey.entity.Question;

/**
 * 管理填空题答案
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface AnFillblankManager extends BaseService<AnFillblank, String>{

	/**
	 * 根据ID查找答案
	 * @param belongAnswerId
	 * @param quId
	 * @return
	 */
	public AnFillblank findAnswer(String belongAnswerId, String quId);

	/**
	 * 问题统计操作
	 * @param question
	 */
	public void findGroupStats(Question question);

	/**
	 * 分页查找填空题的答案
	 * @param page
	 * @param quId
	 * @return
	 */
	Page<AnFillblank> findPage(Page<AnFillblank> page, String quId);
}
