package net.diaowen.dwsurvey.service;

import net.diaowen.common.service.BaseService;
import net.diaowen.dwsurvey.entity.SurveyDetail;

/**
 * 问卷评情
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface SurveyDetailManager extends BaseService<SurveyDetail, String>{
	/**
	 * 根据问卷id获取配置的详细信息
	 * @param surveyId
	 * @return
	 */
	public SurveyDetail getBySurveyId(String surveyId);

	/**
	 * 根据问卷的sid获取问卷的配置信息
	 *
	 * @param sid 问卷的sid
	 * @return 问卷的配置信息
	 */
	SurveyDetail getSurveyDetailBySid(String sid);
	/**
	 * 保存问卷配置的详细信息
	 * @param t
	 */
	public void saveBaseUp(SurveyDetail t);
}
