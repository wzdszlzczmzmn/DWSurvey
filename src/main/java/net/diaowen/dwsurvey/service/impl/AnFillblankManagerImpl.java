package net.diaowen.dwsurvey.service.impl;

import net.diaowen.common.plugs.page.Page;
import net.diaowen.dwsurvey.entity.AnFillblank;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.service.AnFillblankManager;
import net.diaowen.dwsurvey.dao.AnFillblankDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.diaowen.common.service.BaseServiceImpl;

/**
 * 实现对用户填写的问卷答案的管理和查询功能
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class AnFillblankManagerImpl extends BaseServiceImpl<AnFillblank, String> implements AnFillblankManager {

	private final AnFillblankDao anFillblankDao;
	@Autowired
	public AnFillblankManagerImpl(AnFillblankDao anFillblankDao) {
		this.anFillblankDao = anFillblankDao;
	}

	@Override
	public void setBaseDao() {
		this.baseDao=anFillblankDao;
	}

	/**
	 * 根据id查找答案
	 * @param belongAnswerId
	 * @param quId
	 * @return
	 */
	public AnFillblank findAnswer(String belongAnswerId,String quId){
		//belongAnswerId quId
		Criterion criterion1=Restrictions.eq("belongAnswerId", belongAnswerId);
		Criterion criterion2=Restrictions.eq("quId", quId);
		return anFillblankDao.findUnique(criterion1,criterion2);
	}


	/**
	 * 调用dao，通过对数据库访问统计填空题的作答情况
	 * @param question
	 */
	@Override
	public void findGroupStats(Question question) {
		anFillblankDao.findGroupStats(question);
	}

	/**
	 * 分页查找答案
	 * @param page
	 * @param quId
	 * @return
	 */
	@Override
	public Page<AnFillblank> findPage(Page<AnFillblank> page, String quId) {
		Criterion cri1 = Restrictions.eq("quId",quId);
		Criterion cri2 = Restrictions.eq("visibility",1);
		return findPage(page,cri1,cri2);
	}

}
