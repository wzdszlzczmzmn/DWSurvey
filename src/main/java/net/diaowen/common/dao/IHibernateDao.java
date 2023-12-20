package net.diaowen.common.dao;

import java.io.Serializable;
import java.util.List;

import net.diaowen.common.plugs.page.Page;
import net.diaowen.common.plugs.page.PageRequest;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import javax.persistence.criteria.CriteriaQuery;

/**
 * 通用 Hibernate DAO 接口，继承自 ISimpleHibernateDao。
 *
 * @param <T>  实体类型
 * @param <ID> 实体 ID 类型
 */
public interface IHibernateDao<T, ID extends Serializable> extends ISimpleHibernateDao<T, ID> {



	/**
	 * 分页获取全部对象.
	 * @param pageRequest 分页参数.
	 * @return page<T>
	 */
	public abstract Page<T> getAll(final PageRequest pageRequest);

	/**
	 * 按HQL分页查询.
	 *
	 * @param pageRequest 分页参数.
	 * @param hql hql语句.
	 * @param values 数量可变的查询参数,按顺序绑定.
	 *
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	public abstract Page<T> findPage(final PageRequest pageRequest, String hql,
                                     final Object... values);

	/**
	 * 按HQL分页查询.
	 *
	 * @param pageRequest 分页参数.
	 * @param hql hql语句.
	 *
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	public abstract Page<T> findPage(final PageRequest pageRequest, String hql);


	/**
	 * 分页查询。
	 *
	 * @param pageRequest 分页请求
	 * @param criterions  查询条件
	 * @return 分页结果
	 */
	public abstract Page<T> findPage(final PageRequest pageRequest,
                                     final Criterion... criterions);
	/**
	 * 分页查询。
	 *
	 * @param pageRequest 分页请求
	 * @param criterions  查询条件
	 * @return 分页结果
	 */
	public abstract Page<T> findPageList(final PageRequest pageRequest,
                                         final List<Criterion> criterions);
	/**
	 * 分页查询。
	 *
	 * @param pageRequest 分页请求
	 * @param c           查询条件
	 * @return 分页结果
	 */
	public Page<T> findPageCriteria(PageRequest pageRequest, Criteria c);

	/**
	 * action转入id得到模型
	 * @param id
	 * @return 模型
	 */
	public T getModel(ID id);

	/**
	 * 排序并取出一列数据。
	 *
	 * @param orderByProperty 排序属性
	 * @param isAsc           是否升序
	 * @param criterions      查询条件
	 * @return 数据列表
	 */
	public  List<T>  findByOrder(String orderByProperty, boolean isAsc, Criterion... criterions);

	/**
	 * 取出第一条数据。
	 *
	 * @param criterions 查询条件
	 * @return 第一条数据
	 */
	public  T  findFirst(Criterion... criterions);
	/**
	 * 取出第一条数据。
	 *
	 * @param criterions 查询条件
	 * @return 第一条数据
	 */
	public  T  findFirst(List<Criterion> criterions);
	/**
	 * 取出第一条数据。
	 *
	 * @param orderByProperty 排序属性
	 * @param isAsc           是否升序
	 * @param criterions      查询条件
	 * @return 第一条数据
	 */
	public  T  findFirst(String orderByProperty, boolean isAsc, Criterion... criterions);
	/**
	 * 取出第一条数据。
	 *
	 * @param orderByProperty 排序属性
	 * @param isAsc           是否升序
	 * @param criterions      查询条件
	 * @return 第一条数据
	 */
	public  T  findFirst(String orderByProperty, boolean isAsc, List<Criterion> criterions);

	/**
	 * 执行一条 HQL 查询并返回一个 Object 数组。
	 *
	 * @param hql    HQL 查询语句
	 * @param values 查询参数
	 * @return Object 数组
	 */
	public Object findUniObjs(String hql, Object... values);

	/**
	 * 执行一条 HQL 查询并返回一个 List<Object[]>。
	 *
	 * @param hql    HQL 查询语句
	 * @param values 查询参数
	 * @return List<Object[]>
	 */
	public List<Object[]> findList(String hql, Object... values);
	/**
	 * 根据查询条件分页查询。
	 *
	 * @param page         分页请求
	 * @param criterions   查询条件
	 * @return 分页结果
	 */
	public Page<T> findPageByCri(Page<T> page, List<Criterion> criterions);
	/**
	 * 查询所有数据。
	 *
	 * @param criteriaQuery 查询条件
	 * @return 数据列表
	 */
	public List<T> findAll(CriteriaQuery criteriaQuery);

	/**
	 * 按序查找
	 * @param pageRequest 分页请求
	 * @param orderByProperty 属性
	 * @param isAsc 是否升序
	 * @param criterions criterion列表，查询条件
	 * @return 查询结果
	 */
	public Page<T> findPageOderBy(Page<T> pageRequest,String orderByProperty, boolean isAsc, List<Criterion> criterions);

	/**
	 * 按序查找
	 * @param pageRequest 分页请求
	 * @param orderByProperty 属性
	 * @param isAsc 是否升序
	 * @param criterions 查询条件
	 * @return 查询结果
	 */
	public Page<T> findPageOderBy(Page<T> pageRequest,String orderByProperty, boolean isAsc, Criterion... criterions);


	/**
	 * 通过条件查找
	 * @param pageRequest 分页请求
	 * @param c 查询条件
	 * @return 查询结果
	 */
	public abstract Page<T> findPageByCriteria(final PageRequest pageRequest, Criteria c);

}
