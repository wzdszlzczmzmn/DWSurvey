package net.diaowen.dwsurvey.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.diaowen.dwsurvey.entity.QuRadio;
import net.diaowen.dwsurvey.service.QuScoreManager;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.diaowen.common.service.BaseServiceImpl;
import net.diaowen.dwsurvey.dao.QuScoreDao;
import net.diaowen.dwsurvey.entity.QuScore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * 评分题
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class QuScoreManagerImpl extends BaseServiceImpl<QuScore, String> implements QuScoreManager {

	@Autowired
	private QuScoreDao quScoreDao;

	@Override
	public void setBaseDao() {
		this.baseDao=quScoreDao;
	}

	/**
	 * 根据评分题的 id 查找其下的选项
	 * @param quId 问题 id
	 * @return
	 */
	public List<QuScore> findByQuId(String quId){
		/*Page<QuScore> page=new Page<QuScore>();
		page.setOrderBy("orderById");
		page.setOrderDir("asc");

		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_quId", quId));
		filters.add(new PropertyFilter("EQI_visibility", "1"));
		return findAll(page, filters);*/
		CriteriaBuilder criteriaBuilder=quScoreDao.getSession().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(QuScore.class);
		Root root = criteriaQuery.from(QuScore.class);
		criteriaQuery.select(root);
		Predicate eqQuId = criteriaBuilder.equal(root.get("quId"),quId);
		// 只查询可见的选项
		Predicate eqVisibility = criteriaBuilder.equal(root.get("visibility"),1);
		criteriaQuery.where(eqQuId,eqVisibility);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));
		return quScoreDao.findAll(criteriaQuery);
	}

	/**
	 * 根据评分题 id 找到其下序号为 1 的选项
	 * @param quId
	 * @return
	 */
	public int getOrderById(String quId){
		Criterion criterion=Restrictions.eq("quId", quId);
		// TODO: typo 应为 quScore
		QuScore quRadio=quScoreDao.findFirst("orderById", false, criterion);
		if(quRadio!=null){
			return quRadio.getOrderById();
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
	public QuScore upOptionName(String quId,String quItemId, String optionName) {
		if(quItemId!=null && !"".equals(quItemId)){
			QuScore quScore=quScoreDao.get(quItemId);
			quScore.setOptionName(optionName);
			quScoreDao.save(quScore);
			return quScore;
		}else{
			//取orderById
			int orderById=getOrderById(quId);
			//新加选项
			QuScore quScore=new QuScore();
			quScore.setQuId(quId);
			quScore.setOptionName(optionName);
			//title
			quScore.setOrderById(++orderById);
			quScore.setOptionTitle(orderById+"");
			quScoreDao.save(quScore);
			return quScore;
		}
	}

	/**
	 * 为多项填空题添加多个选项
	 * @param quId
	 * @param quScores
	 * @return
	 */
	@Override
	@Transactional
	public List<QuScore> saveManyOptions(String quId,List<QuScore> quScores) {
		//取orderById
		int orderById=getOrderById(quId);
		for (QuScore quScore : quScores) {
			//新加选项
			quScore.setOrderById(++orderById);
			quScore.setOptionTitle(orderById+"");
			quScoreDao.save(quScore);
		}
		return quScores;
	}

	// 根据 id 删除对应选项。（软删除，设置 visibility 为 0）
	@Override
	@Transactional
	public void ajaxDelete(String quItemId) {
		QuScore quScore=get(quItemId);
		quScore.setVisibility(0);
		quScoreDao.save(quScore);
	}

	// 没用
	@Override
	@Transactional
	public void saveAttr(String quItemId) {
		QuScore quScore=get(quItemId);
		quScoreDao.save(quScore);
	}
}
