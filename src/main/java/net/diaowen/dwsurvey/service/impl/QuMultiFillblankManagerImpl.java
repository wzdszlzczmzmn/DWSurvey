package net.diaowen.dwsurvey.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.diaowen.dwsurvey.dao.QuMultiFillblankDao;
import net.diaowen.dwsurvey.entity.QuMultiFillblank;
import net.diaowen.dwsurvey.service.QuMultiFillblankManager;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.diaowen.common.service.BaseServiceImpl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * 多项填空题
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service("quMultiFillblankManager")
public class QuMultiFillblankManagerImpl extends BaseServiceImpl<QuMultiFillblank, String> implements QuMultiFillblankManager {
	@Autowired
	private QuMultiFillblankDao quMultiFillblankDao;

	@Override
	public void setBaseDao() {
		this.baseDao=quMultiFillblankDao;
	}

	// 根据 id 查找多项填空题
	public List<QuMultiFillblank> findByQuId(String quId){
		CriteriaBuilder criteriaBuilder=quMultiFillblankDao.getSession().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(QuMultiFillblank.class);
		Root root = criteriaQuery.from(QuMultiFillblank.class);
		criteriaQuery.select(root);
		// 设置查询条件
		Predicate eqQuId = criteriaBuilder.equal(root.get("quId"),quId);
		Predicate eqVisibility = criteriaBuilder.equal(root.get("visibility"),1);
		criteriaQuery.where(eqQuId,eqVisibility);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));
		return quMultiFillblankDao.findAll(criteriaQuery);
	}

	// 查找多项填空题的最后一个选项的序号。如果找不到，则返回 0
	public int getOrderById(String quId){
		Criterion criterion=Restrictions.eq("quId", quId);
		QuMultiFillblank quMultiFillblank=quMultiFillblankDao.findFirst("orderById", false, criterion);
		if(quMultiFillblank!=null){
			return quMultiFillblank.getOrderById();
		}
		return 0;
	}



	/*******************************************************************8
	 * 更新操作
	 */

	/**
	 *  根据选项 id 设置选项的说明。如果 quItemId 为 null 或空字符串，则新增一个选项
	 * @param quId 选项关联的问题 id
	 * @param quItemId
	 * @param optionName 选项说明
	 * @return
	 */
	@Override
	@Transactional
	public QuMultiFillblank upOptionName(String quId,String quItemId, String optionName) {
		if(quItemId!=null && !"".equals(quItemId)){
			// 根据 id 查询选项
			QuMultiFillblank quMultiFillblank=quMultiFillblankDao.get(quItemId);
			quMultiFillblank.setOptionName(optionName);
			quMultiFillblankDao.save(quMultiFillblank);
			return quMultiFillblank;
		}else{
			//取orderById
			int orderById=getOrderById(quId);
			//新加选项
			QuMultiFillblank quMultiFillblank=new QuMultiFillblank();
			quMultiFillblank.setQuId(quId);
			quMultiFillblank.setOptionName(optionName);
			//title
			quMultiFillblank.setOrderById(++orderById);
			quMultiFillblank.setOptionTitle(orderById+"");
			quMultiFillblankDao.save(quMultiFillblank);
			return quMultiFillblank;
		}
	}

	/**
	 * 为多项填空题添加多个选项
	 * @param quId
	 * @param quMultiFillblanks
	 * @return
	 */
	@Override
	@Transactional
	public List<QuMultiFillblank> saveManyOptions(String quId,List<QuMultiFillblank> quMultiFillblanks) {
		//取orderById
		int orderById=getOrderById(quId);
		for (QuMultiFillblank quMultiFillblank : quMultiFillblanks) {
			//新加选项
			quMultiFillblank.setOrderById(++orderById);
			quMultiFillblank.setOptionTitle(orderById+"");
			quMultiFillblankDao.save(quMultiFillblank);
		}
		return quMultiFillblanks;
	}

	// 根据 id 删除对应选项。（软删除，设置 visibility 为 0）
	@Override
	@Transactional
	public void ajaxDelete(String quItemId) {
		QuMultiFillblank quMultiFillblank=get(quItemId);
		quMultiFillblank.setVisibility(0);
		quMultiFillblankDao.save(quMultiFillblank);
	}

	// 没用
	@Override
	@Transactional
	public void saveAttr(String quItemId) {
		QuMultiFillblank quMultiFillblank=get(quItemId);
		quMultiFillblankDao.save(quMultiFillblank);
	}
}
