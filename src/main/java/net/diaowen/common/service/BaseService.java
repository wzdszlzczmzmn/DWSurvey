package net.diaowen.common.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;

import net.diaowen.common.base.entity.IdEntity;
import net.diaowen.common.plugs.page.Page;

/**
 * 业务基类接口
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 * BaseService接口，用于定义基本的服务操作。
 *
 * @param <T>  实体类型
 * @param <ID> 实体ID类型
 */
public interface BaseService<T extends IdEntity,ID extends Serializable> {
	/**
	 * 设置BaseDao对象。
	 */
	public void setBaseDao();
	/**
	 * 保存实体对象。
	 *
	 * @param t 实体对象
	 */
	public void save(T t);
	/**
	 * 删除实体对象。
	 *
	 * @param t 实体对象
	 */
	public void delete(T t);
	/**
	 * 根据ID删除实体对象。
	 *
	 * @param id 实体ID
	 */
	public void delete(ID id);
	/**
	 * 根据ID获取实体对象。
	 *
	 * @param id 实体ID
	 * @return 实体对象
	 */
	public T get(ID id);
	/**
	 * 根据ID获取实体对象再得到模型。
	 *
	 * @param id 实体ID
	 * @return 实体对象模型
	 */
	public T getModel(ID id);
	/**
	 * 根据ID查找实体对象。
	 *
	 * @param id 实体ID
	 * @return 实体对象
	 */
	public T findById(ID id);
	/**
	 * 根据条件查找实体对象链表。
	 *
	 * @param criterions 条件
	 * @return 实体对象链表
	 */
	public List<T> findList(Criterion... criterions);
	/**
	 * 根据条件查找实体对象分页列表。
	 *
	 * @param page      分页对象
	 * @param criterion 条件
	 * @return 实体对象分页列表
	 */
	public Page<T> findPage(Page<T> page, Criterion... criterion);
}
