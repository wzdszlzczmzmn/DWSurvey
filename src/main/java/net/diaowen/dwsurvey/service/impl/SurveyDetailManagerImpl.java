package net.diaowen.dwsurvey.service.impl;

import net.diaowen.common.service.BaseServiceImpl;
import net.diaowen.common.utils.ReflectionUtils;
import net.diaowen.dwsurvey.dao.SurveyDetailDao;
import net.diaowen.dwsurvey.dao.SurveyDirectoryDao;
import net.diaowen.dwsurvey.entity.SurveyDetail;
import net.diaowen.dwsurvey.entity.SurveyDirectory;
import net.diaowen.dwsurvey.service.SurveyDetailManager;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 问卷详情
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class SurveyDetailManagerImpl extends BaseServiceImpl<SurveyDetail, String> implements SurveyDetailManager{
	/**
	 * 问卷配置信息Dao
	 */
	private final SurveyDetailDao surveyDetailDao;
	/**
	 * 问卷信息Dao
	 */
	private final SurveyDirectoryDao surveyDirectoryDao;

	@Autowired
	public SurveyDetailManagerImpl(SurveyDetailDao surveyDetailDao, SurveyDirectoryDao surveyDirectoryDao) {
		this.surveyDetailDao = surveyDetailDao;
		this.surveyDirectoryDao = surveyDirectoryDao;
	}

	@Override
	public void setBaseDao() {
		this.baseDao=surveyDetailDao;
	}

	/**
	 * 保存问卷配置的详细信息
	 * @param t
	 */
	@Transactional
	@Override
	public void save(SurveyDetail t) {
		//判断有无，有则更新
		SurveyDetail surveyDetail=findUn(t.getDirId());
		if(surveyDetail==null){
			surveyDetail=new SurveyDetail();
		}
		ReflectionUtils.copyAttr(t,surveyDetail);
		super.save(surveyDetail);
	}

	/**
	 * 根据 id 获取配置的详细信息
	 * @param dirId
	 * @return
	 */
	private SurveyDetail findUn(String dirId){
		Criterion criterion=Restrictions.eq("dirId", dirId);
		 List<SurveyDetail> details=surveyDetailDao.find(criterion);
		 if(details!=null && !details.isEmpty()){
			 return details.get(0);
		 }
		 return null;
	}

	/**
	 * 根据问卷id获取配置的详细信息
	 * @param surveyId
	 * @return
	 */
	@Override
	public SurveyDetail getBySurveyId(String surveyId) {
		 return findUn(surveyId);
	}

	/**
	 * 保存问卷配置的详细信息
	 * @param t
	 */
	@Transactional
	@Override
	public void saveBaseUp(SurveyDetail t) {
		//判断有无，有则更新
		SurveyDetail surveyDetail=findUn(t.getDirId());
		if(surveyDetail!=null){

			surveyDetail.setEffective(t.getEffective());
			surveyDetail.setEffectiveIp(t.getEffectiveIp());
			surveyDetail.setRefresh(t.getRefresh());
			surveyDetail.setRule(t.getRule());
			surveyDetail.setRuleCode(t.getRuleCode());
			surveyDetail.setIsRealName(t.getIsRealName());
			surveyDetail.setYnEndTime(t.getYnEndTime());
			surveyDetail.setYnEndNum(t.getYnEndNum());
			surveyDetail.setEndNum(t.getEndNum());
			surveyDetail.setEndTime(t.getEndTime());

			super.save(surveyDetail);
		}

	}
}
