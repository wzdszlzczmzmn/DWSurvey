package net.diaowen.dwsurvey.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.diaowen.dwsurvey.dao.QuCheckboxDao;
import net.diaowen.dwsurvey.entity.Question;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.diaowen.common.plugs.page.Page;
import net.diaowen.common.service.BaseServiceImpl;
import net.diaowen.dwsurvey.entity.QuCheckbox;
import net.diaowen.dwsurvey.service.QuCheckboxManager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 多选题
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service("quCheckboxManager")
public class QuCheckboxManagerImpl extends BaseServiceImpl<QuCheckbox, String> implements QuCheckboxManager {

	@Autowired
	private QuCheckboxDao quCheckboxDao;

	@Override
	public void setBaseDao() {
		this.baseDao=quCheckboxDao;
	}

	/**
	 * 根握多选题 id 查找它的选项
	 * @param quId
	 * @return
	 */
	public List<QuCheckbox> findByQuId(String quId){
		CriteriaBuilder criteriaBuilder=quCheckboxDao.getSession().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(QuCheckbox.class);
		Root root = criteriaQuery.from(QuCheckbox.class);
		criteriaQuery.select(root);
		// 设置查询条件
		Predicate eqQuId = criteriaBuilder.equal(root.get("quId"),quId);
		Predicate eqVisibility = criteriaBuilder.equal(root.get("visibility"),1);
		criteriaQuery.where(eqQuId,eqVisibility);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));
		return quCheckboxDao.findAll(criteriaQuery);

	}


	/**
	 * 根据多选题 id 查找它的最后一个选项的序号
	 * @param quId
	 * @return
	 */
	public int getOrderById(String quId){
		Criterion criterion=Restrictions.eq("quId", quId);
		// 倒序排序，取最后一个选项的序号
		QuCheckbox quCheckbox=quCheckboxDao.findFirst("orderById", false, criterion);
		if(quCheckbox!=null){
			return quCheckbox.getOrderById();
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
	public QuCheckbox upOptionName(String quId,String quItemId, String optionName) {
		if(quItemId!=null && !"".equals(quItemId)){
			// 根据 id 查询选项
			QuCheckbox quCheckbox=quCheckboxDao.get(quItemId);
			quCheckbox.setOptionName(optionName);
			quCheckboxDao.save(quCheckbox);
			return quCheckbox;
		}else{
			//取orderById
			int orderById=getOrderById(quId);
			//新加选项
			QuCheckbox quCheckbox=new QuCheckbox();
			quCheckbox.setQuId(quId);
			quCheckbox.setOptionName(optionName);
			//title
			quCheckbox.setOrderById(++orderById);
			quCheckbox.setOptionTitle(orderById+"");
			quCheckboxDao.save(quCheckbox);
			return quCheckbox;
		}
	}

	/**
	 * 给多选题添加多个选项
	 * @param quId
	 * @param quCheckboxs
	 * @return
	 */
	@Override
	@Transactional
	public List<QuCheckbox> saveManyOptions(String quId,List<QuCheckbox> quCheckboxs) {
		//取orderById
		int orderById=getOrderById(quId);
		for (QuCheckbox quCheckbox : quCheckboxs) {
			//新加选项
			quCheckbox.setOrderById(++orderById);
			quCheckbox.setOptionTitle(orderById+"");
			quCheckboxDao.save(quCheckbox);
		}
		return quCheckboxs;
	}

	// 根据 id 删除对应选项。（软删除，设置 visibility 为 0）
	@Override
	@Transactional
	public void ajaxDelete(String quItemId) {
		QuCheckbox quCheckbox=get(quItemId);
		quCheckbox.setVisibility(0);
		quCheckboxDao.save(quCheckbox);
	}

	/**
	 * 根据选项 id 设置选项是否是说明
	 * @param quItemId
	 * @param isNote
	 */
	@Override
	@Transactional
	public void saveAttr(String quItemId, String isNote) {
		QuCheckbox quCheckbox=get(quItemId);
		if(isNote!=null && "1".equals(isNote)){
			quCheckbox.setIsNote(1);
		}else{
			quCheckbox.setIsNote(0);
		}
		quCheckboxDao.save(quCheckbox);
	}

}
