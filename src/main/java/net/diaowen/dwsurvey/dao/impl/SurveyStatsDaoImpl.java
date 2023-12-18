package net.diaowen.dwsurvey.dao.impl;

import net.diaowen.dwsurvey.entity.SurveyStats;
import org.springframework.stereotype.Repository;

import net.diaowen.common.dao.BaseDaoImpl;
import net.diaowen.common.QuType;
import net.diaowen.dwsurvey.dao.SurveyStatsDao;
import net.diaowen.dwsurvey.entity.Question;

/**
 * 问卷统计 dao
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */

@Repository
public class SurveyStatsDaoImpl extends BaseDaoImpl<SurveyStats, String> implements SurveyStatsDao{


	/**
	 *
	 * @param rowQuestion
	 * @param colQuestion
	 */
	@Override
	public void findStatsDataCross(Question rowQuestion, Question colQuestion) {
		
		getQuItemName(rowQuestion);
		getQuItemName(colQuestion);
		
		QuType quType=rowQuestion.getQuType();
		if(quType==QuType.RADIO){
			radioRowDataCross(rowQuestion,colQuestion);
		}else if(quType==QuType.CHECKBOX){
			checkboxRowDataCross(rowQuestion,colQuestion);
		}
		
	}
	
	private void checkboxRowDataCross(Question rowQuestion, Question colQuestion) {
		// TODO Auto-generated method stub
		
	}

	private void radioRowDataCross(Question rowQuestion, Question colQuestion) {
		// TODO Auto-generated method stub
		
	}

	private String getQuItemName(Question question){
		String result=null;
		QuType quType=question.getQuType();
		if(quType==QuType.YESNO){
			result="yesno_answer";
		}else if(quType==QuType.RADIO){
			result="qu_item_id";
		}
		return result;
	}
	
}
