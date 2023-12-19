package net.diaowen.dwsurvey.service.impl;

import net.diaowen.common.service.BaseServiceImpl;
import net.diaowen.dwsurvey.dao.AnUploadFileDao;
import net.diaowen.dwsurvey.entity.AnUplodFile;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.service.AnUploadFileManager;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 填空题 对上传文件问题回答的统计与查找
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class AnUploadFileManagerImpl extends BaseServiceImpl<AnUplodFile, String> implements AnUploadFileManager {

	private AnUploadFileDao anUploadFileDao;

	@Autowired
	public AnUploadFileManagerImpl(AnUploadFileDao anUploadFileDao) {
		this.anUploadFileDao = anUploadFileDao;
	}

	@Override
	public void setBaseDao() {
		this.baseDao=anUploadFileDao;
	}


	//根据exam_user信息查询答案
	/**
	 * 在数据库中查找答案对象
	 * @param belongAnswerId
	 * @param quId
	 * @return
	 */
	public List<AnUplodFile> findAnswer(String belongAnswerId, String quId){
		//belongAnswerId quId
		Criterion criterion1=Restrictions.eq("belongAnswerId", belongAnswerId);
		Criterion criterion2=Restrictions.eq("quId", quId);
		return anUploadFileDao.find(criterion1,criterion2);
	}

	/**
	 * 统计问题的已回答数和未回答数
	 * @param question
	 */
	@Override
	public void findGroupStats(Question question) {
		anUploadFileDao.findGroupStats(question);
	}

	/**
	 * 获取指定调查的回答文件
	 * @param surveyId
	 * @return
	 */
	public List<AnUplodFile> findAnswer(String surveyId){
		//belongAnswerId quId
		Criterion criterion1=Restrictions.eq("belongId", surveyId);
		return anUploadFileDao.find(criterion1);
	}

}
