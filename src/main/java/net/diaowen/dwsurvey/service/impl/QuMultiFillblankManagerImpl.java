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
 * 多项填空题 填空项业务 实现类
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

	/**
	 * 根据题目ID查找多项填空题填空项
	 * @param quId 题目ID
	 * @return 返回多项填空题填空项列表
	 */
	public List<QuMultiFillblank> findByQuId(String quId){
		/*Page<QuMultiFillblank> page=new Page<QuMultiFillblank>();
		page.setOrderBy("orderById");
		page.setOrderDir("asc");

		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_quId", quId));
		filters.add(new PropertyFilter("EQI_visibility", "1"));
		return findAll(page, filters);
		*/
		CriteriaBuilder criteriaBuilder=quMultiFillblankDao.getSession().getCriteriaBuilder();// 获取 CriteriaBuilder 对象，用于构建查询条件
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(QuMultiFillblank.class);// 创建一个 CriteriaQuery 对象，用于执行查询操作
		Root root = criteriaQuery.from(QuMultiFillblank.class);// 从查询中指定要查询的 QuMultiFillblank 类
		criteriaQuery.select(root); // 设置查询所需的属性
		// 添加查询条件:quId 和 visibility 属性
		Predicate eqQuId = criteriaBuilder.equal(root.get("quId"),quId);
		Predicate eqVisibility = criteriaBuilder.equal(root.get("visibility"),1);

		criteriaQuery.where(eqQuId,eqVisibility);// 将查询条件添加到查询中
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));// 设置查询结果的排序方式
		return quMultiFillblankDao.findAll(criteriaQuery);// 执行查询并返回结果
	}

	/**
	 * 获取多项填空题填空项的 orderById
	 * @param quId 题目ID
	 * @return 返回多项填空题填空项的 orderById
	 */
	public int getOrderById(String quId){
		Criterion criterion=Restrictions.eq("quId", quId);// 创建一个 Criterion 对象，用于查询条件
		QuMultiFillblank quMultiFillblank=quMultiFillblankDao.findFirst("orderById", false, criterion);// 根据给定的条件查找第一个 QuMultiFillblank 对象
		if(quMultiFillblank!=null){// 如果找到了对应的 QuMultiFillblank 对象，则返回其 orderById 属性
			return quMultiFillblank.getOrderById();
		}
		return 0;// 如果没有找到，则返回 0
	}



	/*******************************************************************8
	 * 更新操作
	 */

	@Override
	@Transactional
	public QuMultiFillblank upOptionName(String quId,String quItemId, String optionName) {
		// 如果 quItemId 不为空且不为空字符串，则更新现有的选项
		if(quItemId!=null && !"".equals(quItemId)){
			QuMultiFillblank quMultiFillblank=quMultiFillblankDao.get(quItemId);
			quMultiFillblank.setOptionName(optionName);
			quMultiFillblankDao.save(quMultiFillblank);
			return quMultiFillblank;
		}else{
			// 否则，根据题目ID获取当前最大的 orderById
			int orderById=getOrderById(quId);

			// 新建一个 QuMultiFillblank 对象，设置其题目ID、选项名称、orderById 和选项标题
			QuMultiFillblank quMultiFillblank=new QuMultiFillblank();
			quMultiFillblank.setQuId(quId);
			quMultiFillblank.setOptionName(optionName);
			quMultiFillblank.setOrderById(++orderById);
			quMultiFillblank.setOptionTitle(orderById+"");

			quMultiFillblankDao.save(quMultiFillblank);// 将新选项保存到数据库中
			return quMultiFillblank;// 返回新创建的 QuMultiFillblank 对象
		}
	}

	@Override
	@Transactional
	public List<QuMultiFillblank> saveManyOptions(String quId,List<QuMultiFillblank> quMultiFillblanks) {
		int orderById=getOrderById(quId);// 获取orderById
		for (QuMultiFillblank quMultiFillblank : quMultiFillblanks) {
			//为新加的选项设置orderById和optionTitle
			quMultiFillblank.setOrderById(++orderById);
			quMultiFillblank.setOptionTitle(orderById+"");

			quMultiFillblankDao.save(quMultiFillblank);// 保存选项
		}
		return quMultiFillblanks;
	}

	@Override
	@Transactional
	public void ajaxDelete(String quItemId) {
		QuMultiFillblank quMultiFillblank=get(quItemId);// 根据ID获取选项
		quMultiFillblank.setVisibility(0);// 设置选项的可见性为0（即不可见）
		quMultiFillblankDao.save(quMultiFillblank);// 保存更改后的选项
	}


	@Override
	@Transactional
	public void saveAttr(String quItemId) {
		QuMultiFillblank quMultiFillblank=get(quItemId);//通过 quItemId 来获取 QuMultiFillblank 对象
		quMultiFillblankDao.save(quMultiFillblank);//将获取到的 QuMultiFillblank 对象保存到数据库中
	}
}
