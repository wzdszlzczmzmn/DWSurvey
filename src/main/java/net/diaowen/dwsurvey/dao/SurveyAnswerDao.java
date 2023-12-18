package net.diaowen.dwsurvey.dao;

import java.util.Map;

import net.diaowen.common.dao.BaseDao;
import net.diaowen.dwsurvey.entity.SurveyStats;
import net.diaowen.dwsurvey.entity.SurveyAnswer;

public interface SurveyAnswerDao extends BaseDao<SurveyAnswer, String>{
	/**
	 * 保存回答
	 * @param surveyAnswer
	 * @param quMaps
	 */
	public void saveAnswer(SurveyAnswer surveyAnswer,
                           Map<String, Map<String, Object>> quMaps);
	/**
	 * 获取单个问卷的全局统计信息
	 * @param surveyStats
	 * @return
	 */
	public SurveyStats surveyStatsData(SurveyStats surveyStats);
	/**
	 * count 结果数量
	 * @param id
	 * @return
	 */
    Long countResult(String id);
}
