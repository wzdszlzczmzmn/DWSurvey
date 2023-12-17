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
 * 排序题业务 实现类
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
	 * 根据题目ID查找排序题选项
	 * @param quId 题目ID
	 * @return 返回排序题选项列表
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
		CriteriaBuilder criteriaBuilder=quOrderbyDao.getSession().getCriteriaBuilder();// 获取 CriteriaBuilder 对象，用于构建查询条件
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(QuOrderby.class);// 创建一个 CriteriaQuery 对象，用于执行查询操作
		Root root = criteriaQuery.from(QuOrderby.class);// 从查询中指定要查询的 QuOrderby 类
		criteriaQuery.select(root); // 设置查询所需的属性
		// 添加查询条件:quId 和 visibility 属性
		Predicate eqQuId = criteriaBuilder.equal(root.get("quId"),quId);
		Predicate eqVisibility = criteriaBuilder.equal(root.get("visibility"),1);

		criteriaQuery.where(eqQuId,eqVisibility);// 将查询条件添加到查询中
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));// 设置查询结果的排序方式
		return quOrderbyDao.findAll(criteriaQuery);// 执行查询并返回结果
	}

	/**
	 * 获取排序题选项的 orderById
	 * @param quId 题目ID
	 * @return 返回排序题选项的 orderById
	 */
	public int getOrderById(String quId){
		Criterion criterion=Restrictions.eq("quId", quId);// 创建一个 Criterion 对象，用于查询条件
		QuOrderby quOrderby=quOrderbyDao.findFirst("orderById", false, criterion);// 根据给定的条件查找第一个 QuOrderby 对象
		if(quOrderby!=null){// 如果找到了对应的 QuOrderby 对象，则返回其 orderById 属性
			return quOrderby.getOrderById();
		}
		return 0;// 如果没有找到，则返回 0
	}



	/*******************************************************************8
	 * 更新操作
	 */

	@Override
	@Transactional
	public QuOrderby upOptionName(String quId,String quItemId, String optionName) {
		// 如果 quItemId 不为空且不为空字符串，则更新现有的选项
		if(quItemId!=null && !"".equals(quItemId)){
			QuOrderby quOrderby=quOrderbyDao.get(quItemId);
			quOrderby.setOptionName(optionName);
			quOrderbyDao.save(quOrderby);
			return quOrderby;
		}else{
			// 否则，根据题目ID获取当前最大的 orderById
			int orderById=getOrderById(quId);

			// 新建一个 QuOrderby 对象，设置其题目ID、选项名称、orderById 和选项标题
			QuOrderby quOrderby=new QuOrderby();
			quOrderby.setQuId(quId);
			quOrderby.setOptionName(optionName);
			quOrderby.setOrderById(++orderById);
			quOrderby.setOptionTitle(orderById+"");

			quOrderbyDao.save(quOrderby);// 将新选项保存到数据库中
			return quOrderby;// 返回新创建的 QuOrderby 对象
		}
	}

	@Override
	@Transactional
	public List<QuOrderby> saveManyOptions(String quId,List<QuOrderby> quOrderbys) {
		int orderById=getOrderById(quId);// 获取orderById
		for (QuOrderby quOrderby : quOrderbys) {
			//为新加的选项设置orderById和optionTitle
			quOrderby.setOrderById(++orderById);
			quOrderby.setOptionTitle(orderById+"");

			quOrderbyDao.save(quOrderby);// 保存选项
		}
		return quOrderbys;
	}

	@Override
	@Transactional
	public void ajaxDelete(String quItemId) {
		QuOrderby quOrderby=get(quItemId);// 根据ID获取选项
		quOrderby.setVisibility(0);// 设置选项的可见性为0（即不可见）
		quOrderbyDao.save(quOrderby);// 保存更改后的选项
	}

	@Override
	@Transactional
	public void saveAttr(String quItemId) {
		QuOrderby quOrderby=get(quItemId);// 根据ID获取选项
		quOrderbyDao.save(quOrderby);// 保存选项
	}
}
