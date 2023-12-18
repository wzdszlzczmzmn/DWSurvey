package net.diaowen.dwsurvey.service.impl;


import java.util.List;

import net.diaowen.dwsurvey.dao.QuCheckboxDao;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import net.diaowen.common.service.BaseServiceImpl;
import net.diaowen.dwsurvey.entity.QuCheckbox;
import net.diaowen.dwsurvey.service.QuCheckboxManager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 多选题选项业务 实现类
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
	 * 根据题目ID查找多选题选项
	 * @param quId 题目ID
	 * @return 返回多选题选项列表
	 */
	public List<QuCheckbox> findByQuId(String quId){
		CriteriaBuilder criteriaBuilder=quCheckboxDao.getSession().getCriteriaBuilder();// 获取 CriteriaBuilder 对象，用于构建查询条件
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(QuCheckbox.class);// 创建一个 CriteriaQuery 对象，用于执行查询操作
		Root root = criteriaQuery.from(QuCheckbox.class);// 从查询中指定要查询的 QuCheckbox 类
		criteriaQuery.select(root);// 设置查询所需的属性
		// 添加查询条件:quId 和 visibility 属性
		Predicate eqQuId = criteriaBuilder.equal(root.get("quId"),quId);
		Predicate eqVisibility = criteriaBuilder.equal(root.get("visibility"),1);

		criteriaQuery.where(eqQuId,eqVisibility);// 将查询条件添加到查询中
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));// 设置查询结果的排序方式
		return quCheckboxDao.findAll(criteriaQuery);// 执行查询并返回结果

	}


	/**
	 * 获取多选题选项的 orderById
	 * @param quId 题目ID
	 * @return 返回多选题选项的 orderById
	 */
	public int getOrderById(String quId){
		Criterion criterion=Restrictions.eq("quId", quId);// 创建一个 Criterion 对象，用于查询条件
		QuCheckbox quCheckbox=quCheckboxDao.findFirst("orderById", false, criterion);// 根据给定的条件查找第一个 QuCheckbox 对象
		if(quCheckbox!=null){// 如果找到了对应的 QuCheckbox 对象，则返回其 orderById 属性
			return quCheckbox.getOrderById();
		}
		return 0;// 如果没有找到，则返回 0
	}

	/*******************************************************************8
	 * 更新操作
	 */


	@Override
	@Transactional
	public QuCheckbox upOptionName(String quId,String quItemId, String optionName) {
		// 如果 quItemId 不为空且不为空字符串，则更新现有的选项
		if(quItemId!=null && !"".equals(quItemId)){
			QuCheckbox quCheckbox=quCheckboxDao.get(quItemId);
			quCheckbox.setOptionName(optionName);
			quCheckboxDao.save(quCheckbox);
			return quCheckbox;
		}else{
			// 否则，根据题目ID获取当前最大的 orderById
			int orderById=getOrderById(quId);

			// 新建一个 QuCheckbox 对象，设置其题目ID、选项名称、orderById 和选项标题
			QuCheckbox quCheckbox=new QuCheckbox();
			quCheckbox.setQuId(quId);
			quCheckbox.setOptionName(optionName);
			quCheckbox.setOrderById(++orderById);
			quCheckbox.setOptionTitle(orderById+"");

			quCheckboxDao.save(quCheckbox);	// 将新选项保存到数据库中
			return quCheckbox;// 返回新创建的 QuCheckbox 对象
		}
	}


	@Override
	@Transactional
	public List<QuCheckbox> saveManyOptions(String quId,List<QuCheckbox> quCheckboxs) {
		int orderById=getOrderById(quId);// 获取orderById
		for (QuCheckbox quCheckbox : quCheckboxs) {
			//为新加的选项设置orderById和optionTitle
			quCheckbox.setOrderById(++orderById);
			quCheckbox.setOptionTitle(orderById+"");

			quCheckboxDao.save(quCheckbox);// 保存选项
		}
		return quCheckboxs;
	}


	@Override
	@Transactional
	public void ajaxDelete(String quItemId) {
		QuCheckbox quCheckbox=get(quItemId);// 根据ID获取选项
		quCheckbox.setVisibility(0);// 设置选项的可见性为0（即不可见）
		quCheckboxDao.save(quCheckbox);// 保存更改后的选项
	}


	@Override
	@Transactional
	public void saveAttr(String quItemId, String isNote) {
		QuCheckbox quCheckbox=get(quItemId);// 根据ID获取选项
		// 根据isNote的值设置isNote属性
		if(isNote!=null && "1".equals(isNote)){
			quCheckbox.setIsNote(1);
		}else{
			quCheckbox.setIsNote(0);
		}
		quCheckboxDao.save(quCheckbox);// 保存选项
	}

}
