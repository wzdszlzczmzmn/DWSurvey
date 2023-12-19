package net.diaowen.dwsurvey.service.impl;

import java.util.List;

import net.diaowen.dwsurvey.service.AnScoreManager;
import net.diaowen.dwsurvey.dao.AnScoreDao;
import net.diaowen.dwsurvey.entity.AnScore;
import net.diaowen.dwsurvey.entity.Question;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.diaowen.common.service.BaseServiceImpl;

/**
 * 评分题 答案查找以及统计
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class AnScoreManagerImpl extends BaseServiceImpl<AnScore, String> implements AnScoreManager {

	private AnScoreDao anScoreDao;

	@Autowired
	public AnScoreManagerImpl(AnScoreDao anScoreDao) {
		this.anScoreDao = anScoreDao;
	}

	@Override
	public void setBaseDao() {
		this.baseDao=anScoreDao;
	}

	/**
	 * 在数据库中查询答案对象
	 * @param belongAnswerId
	 * @param quId
	 * @return
	 */
	@Override
	public List<AnScore> findAnswer(String belongAnswerId, String quId) {//belongAnswerId quId
		Criterion criterion1=Restrictions.eq("belongAnswerId", belongAnswerId);
		Criterion criterion2=Restrictions.eq("quId", quId);
		return anScoreDao.find(criterion1,criterion2);
	}

	/**
	 * 评分题各分数的答卷数量、答卷总数量、答卷平均分
	 * @param question
	 */
	@Override
	public void findGroupStats(Question question) {
		anScoreDao.findGroupStats(question);
	}

}
