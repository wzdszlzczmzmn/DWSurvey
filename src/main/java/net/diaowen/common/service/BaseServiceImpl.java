package net.diaowen.common.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.springframework.transaction.annotation.Transactional;

import net.diaowen.common.base.entity.IdEntity;
import net.diaowen.common.dao.BaseDao;
import net.diaowen.common.plugs.page.Page;

/**
 * 业务基类
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 * BaseServiceImpl抽象类，用于实现BaseService接口。
 *
 * @param <T>  实体类型
 * @param <ID> 实体ID类型
 */
@Transactional
public abstract class BaseServiceImpl<T extends IdEntity, ID extends Serializable>
		implements BaseService<T, ID> {

	protected BaseDao<T, ID> baseDao;
	/**
	 * 获取BaseDao对象。
	 *
	 * @return BaseDao对象
	 */
	protected BaseDao<T, ID> getBaseDao() {
		if (baseDao == null) {
			setBaseDao();
		}
		return baseDao;
	}
	/**
	 * 保存实体对象。
	 *
	 * @param t 实体对象
	 */
	@Override
	public void save(T t) {
		String id = t.getId();
		if (id != null && "".equals(id)) {
			t.setId(null);
		}
		getBaseDao().save(t);
	}
	/**
	 * 删除实体对象。
	 *
	 * @param t 实体对象
	 */
	@Override
	public void delete(T t) {
		getBaseDao().delete(t);
	}
	/**
	 * 根据ID删除实体对象。
	 *
	 * @param id 实体ID
	 */
	public void delete(ID id) {
		getBaseDao().delete(get(id));
	};
	/**
	 * 根据ID获取实体对象。
	 *
	 * @param id 实体ID
	 * @return 实体对象
	 */
	public T get(ID id) {
		return getBaseDao().get(id);
	}
	/**
	 * 根据ID获取实体对象再得到模型。
	 *
	 * @param id 实体ID
	 * @return 实体对象的模型
	 */
	public T getModel(ID id) {
		return getBaseDao().getModel(id);
	};
	/**
	 * 根据ID查找实体对象。
	 *
	 * @param id 实体ID
	 * @return 实体对象
	 */
	@Override
	public T findById(ID id) {
		return getBaseDao().findUniqueBy("id",id);
	}
	/**
	 * 根据条件查找实体对象列表。
	 *
	 * @param criterions 条件
	 * @return 实体对象列表
	 */
	@Override
	public List<T> findList(Criterion... criterions) {
		return getBaseDao().find(criterions);
	}
	/**
	 * 根据条件查找实体对象分页列表。
	 *
	 * @param page      分页对象
	 * @param criterions 条件
	 * @return 实体对象分页列表
	 */
	@Override
	public Page<T> findPage(Page<T> page, Criterion... criterions) {
		return getBaseDao().findPage(page, criterions);
	}
	/**
	 * 根据条件链表查找实体对象分页列表。
	 *
	 * @param page       分页对象
	 * @param criterions 条件链表
	 * @return 实体对象分页列表
	 */
	public Page<T> findPageByCri(Page<T> page, List<Criterion> criterions) {
		return getBaseDao().findPageByCri(page, criterions);
	}

}
