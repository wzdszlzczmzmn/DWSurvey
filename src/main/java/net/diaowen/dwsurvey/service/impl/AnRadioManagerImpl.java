package net.diaowen.dwsurvey.service.impl;

import java.util.List;

import net.diaowen.dwsurvey.entity.AnRadio;
import net.diaowen.dwsurvey.dao.AnRadioDao;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.service.AnRadioManager;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.diaowen.common.service.BaseServiceImpl;
import net.diaowen.dwsurvey.entity.DataCross;

/**
 * 处理单选题数据，统计和分析回答情况
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class AnRadioManagerImpl extends BaseServiceImpl<AnRadio, String> implements AnRadioManager {

	private AnRadioDao anRadioDao;
	@Autowired
	public AnRadioManagerImpl(AnRadioDao anRadioDao) {
		this.anRadioDao = anRadioDao;
	}

	@Override
	public void setBaseDao() {
		this.baseDao=anRadioDao;
	}

	/**
	 * 根据id查看问卷的作答
	 * @param belongAnswerId
	 * @param quId
	 * @return
	 */
	public AnRadio findAnswer(String belongAnswerId,String quId){
		//belongAnswerId quId
		Criterion criterion1=Restrictions.eq("belongAnswerId", belongAnswerId);
		Criterion criterion2=Restrictions.eq("quId", quId);
		return anRadioDao.findUnique(criterion1,criterion2);
	}

	/**
	 *统计该选择题回答的数量
	 * @param question
	 */
	@Override
	public void findGroupStats(Question question) {
		anRadioDao.findGroupStats(question);
	}

	/**
	 *获取两个问题的交叉数据
	 * @param rowQuestion
	 * @param colQuestion
	 * @return
	 */
	@Override
	public List<DataCross> findStatsDataCross(Question rowQuestion,
											  Question colQuestion) {
		return anRadioDao.findStatsDataCross(rowQuestion, colQuestion);
	}

	/**
	 * 单选题选项统计结果的列表。
	 * @param question
	 * @return
	 */
	@Override
	public List<DataCross> findStatsDataChart(Question question) {
		return anRadioDao.findStatsDataChart(question);
	}
}
