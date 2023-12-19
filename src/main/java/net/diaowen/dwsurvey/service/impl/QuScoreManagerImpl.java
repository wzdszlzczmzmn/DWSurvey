package net.diaowen.dwsurvey.service.impl;

import java.util.List;

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
 * 评分题选项的增删改查
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class QuScoreManagerImpl extends BaseServiceImpl<QuScore, String> implements QuScoreManager {

	private QuScoreDao quScoreDao;

	@Autowired
	public QuScoreManagerImpl(QuScoreDao quScoreDao) {
		this.quScoreDao = quScoreDao;
	}

	@Override
	public void setBaseDao() {
		this.baseDao=quScoreDao;
	}

	/**
	 * 根据quId查询数据库中的QuScore
	 * @param quId
	 * @return
	 */
	public List<QuScore> findByQuId(String quId){
		CriteriaBuilder criteriaBuilder=quScoreDao.getSession().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(QuScore.class);
		Root root = criteriaQuery.from(QuScore.class);
		criteriaQuery.select(root);
		Predicate eqQuId = criteriaBuilder.equal(root.get("quId"),quId);
		Predicate eqVisibility = criteriaBuilder.equal(root.get("visibility"),1);
		criteriaQuery.where(eqQuId,eqVisibility);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));
		return quScoreDao.findAll(criteriaQuery);
	}

	/**
	 * 根据quId查询数据库中的QuScore对象，返回其OrderById值
	 * @param quId
	 * @return
	 */
	public int getOrderById(String quId){
		Criterion criterion=Restrictions.eq("quId", quId);
		QuScore quRadio=quScoreDao.findFirst("orderById", false, criterion);
		if(quRadio!=null){
			return quRadio.getOrderById();
		}
		return 0;
	}


	/**
	 * 更新或添加选项
	 * @param quId
	 * @param quItemId
	 * @param optionName
	 * @return
	 */
	@Override
	@Transactional
	public QuScore upOptionName(String quId,String quItemId, String optionName) {
		//选项存在
		if(quItemId!=null && !"".equals(quItemId)){
			QuScore quScore=quScoreDao.get(quItemId);
			quScore.setOptionName(optionName);
			quScoreDao.save(quScore);
			return quScore;
		}else{//选项不存在
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
	 * 批量向数据库保存问卷选项
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

	/**
	 * 删除问卷选项
	 * @param quItemId
	 */
	@Override
	@Transactional
	public void ajaxDelete(String quItemId) {
		QuScore quScore=get(quItemId);
		quScore.setVisibility(0);
		quScoreDao.save(quScore);
	}

	/**
	 * 更新问卷选项至数据库
	 * @param quItemId
	 */
	@Override
	@Transactional
	public void saveAttr(String quItemId) {
		QuScore quScore=get(quItemId);
		quScoreDao.save(quScore);
	}
}
