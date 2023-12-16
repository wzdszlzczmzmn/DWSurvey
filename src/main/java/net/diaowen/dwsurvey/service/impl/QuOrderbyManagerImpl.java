package net.diaowen.dwsurvey.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.diaowen.dwsurvey.dao.QuOrderbyDao;
import net.diaowen.dwsurvey.entity.QuMultiFillblank;
import net.diaowen.dwsurvey.entity.QuOrderby;
import net.diaowen.dwsurvey.service.QuOrderbyManager;
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
 * 排序题
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service
public class QuOrderbyManagerImpl extends BaseServiceImpl<QuOrderby, String> implements QuOrderbyManager {

	@Autowired
	private QuOrderbyDao quOrderbyDao;

	@Override
	public void setBaseDao() {
		this.baseDao=quOrderbyDao;
	}

	/**
	 * 根据 id 查找相应的排序题
	 * @param quId
	 * @return
	 */
	public List<QuOrderby> findByQuId(String quId){
		/*Page<QuOrderby> page=new Page<QuOrderby>();
		page.setOrderBy("orderById");
		page.setOrderDir("asc");

		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_quId", quId));
		filters.add(new PropertyFilter("EQI_visibility", "1"));
		return findAll(page, filters);
		*/
		CriteriaBuilder criteriaBuilder=quOrderbyDao.getSession().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(QuOrderby.class);
		Root root = criteriaQuery.from(QuOrderby.class);
		criteriaQuery.select(root);
		Predicate eqQuId = criteriaBuilder.equal(root.get("quId"),quId);
		Predicate eqVisibility = criteriaBuilder.equal(root.get("visibility"),1);
		criteriaQuery.where(eqQuId,eqVisibility);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));
		return quOrderbyDao.findAll(criteriaQuery);
	}

	// 查找排序题最后一个选项的序号
	public int getOrderById(String quId){
		Criterion criterion=Restrictions.eq("quId", quId);
		QuOrderby quOrderby=quOrderbyDao.findFirst("orderById", false, criterion);
		if(quOrderby!=null){
			return quOrderby.getOrderById();
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
	public QuOrderby upOptionName(String quId,String quItemId, String optionName) {
		if(quItemId!=null && !"".equals(quItemId)){
			QuOrderby quOrderby=quOrderbyDao.get(quItemId);
			quOrderby.setOptionName(optionName);
			quOrderbyDao.save(quOrderby);
			return quOrderby;
		}else{
			//取orderById
			int orderById=getOrderById(quId);
			//新加选项
			QuOrderby quOrderby=new QuOrderby();
			quOrderby.setQuId(quId);
			quOrderby.setOptionName(optionName);
			//title
			quOrderby.setOrderById(++orderById);
			quOrderby.setOptionTitle(orderById+"");
			quOrderbyDao.save(quOrderby);
			return quOrderby;
		}
	}


	/**
	 * 为排序题添加多个选项
	 * @param quId
	 * @param quOrderbys
	 * @return
	 */
	@Override
	@Transactional
	public List<QuOrderby> saveManyOptions(String quId,List<QuOrderby> quOrderbys) {
		//取orderById
		int orderById=getOrderById(quId);
		for (QuOrderby quOrderby : quOrderbys) {
			//新加选项
			quOrderby.setOrderById(++orderById);
			quOrderby.setOptionTitle(orderById+"");
			quOrderbyDao.save(quOrderby);
		}
		return quOrderbys;
	}

	// 根据 id 删除对应选项。（软删除，设置 visibility 为 0）
	@Override
	@Transactional
	public void ajaxDelete(String quItemId) {
		QuOrderby quOrderby=get(quItemId);
		quOrderby.setVisibility(0);
		quOrderbyDao.save(quOrderby);
	}

	// 没用
	@Override
	@Transactional
	public void saveAttr(String quItemId) {
		QuOrderby quOrderby=get(quItemId);
		quOrderbyDao.save(quOrderby);
	}
}
