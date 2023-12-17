package net.diaowen.dwsurvey.service.impl;

import java.util.List;

import net.diaowen.common.plugs.page.Page;
import net.diaowen.dwsurvey.dao.AnDFillblankDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.diaowen.common.service.BaseServiceImpl;
import net.diaowen.dwsurvey.entity.AnDFillblank;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.service.AnDFillblankManager;

/**
 * 多项填空题业务 实现类
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class AnDFillblankManagerImpl extends BaseServiceImpl<AnDFillblank, String> implements AnDFillblankManager{

	@Autowired
	private AnDFillblankDao anDFillblankDao;

	@Override
	public void setBaseDao() {
		this.baseDao=anDFillblankDao;
	}

	/**
	 * 根据exam_user信息查询答案
	 *
	 * @param belongAnswerId 所属答案的ID
	 * @param quId 题目ID
	 * @return 返回多项填空题的答案列表
	 */
	public List<AnDFillblank> findAnswer(String belongAnswerId,String quId){
		//belongAnswerId quId
		Criterion criterion1=Restrictions.eq("belongAnswerId", belongAnswerId);
		Criterion criterion2=Restrictions.eq("quId", quId);
		return anDFillblankDao.find(criterion1,criterion2);
	}


	@Override
	public void findGroupStats(Question question) {
		anDFillblankDao.findGroupStats(question);
	}


	/**
	 * 分页查询
	 *
	 * @param page 分页对象，用于定义分页属性（页码、每页大小等）
	 * @param quItemId 填空项ID
	 * @return 分页的答案列表
	 */
	public Page<AnDFillblank> findPage(Page<AnDFillblank> page, String quItemId){
		Criterion cri1 = Restrictions.eq("quItemId",quItemId);
		Criterion cri2 = Restrictions.eq("visibility",1);
		return findPage(page,cri1,cri2);
	}
}
