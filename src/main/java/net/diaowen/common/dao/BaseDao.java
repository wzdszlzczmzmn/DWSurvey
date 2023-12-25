package net.diaowen.common.dao;

import java.io.Serializable;


/**
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 * BaseDao接口，继承IHibernateDao接口。
 *
 * @param <T>  实体类型
 * @param <ID> 实体ID类型
 */
public interface BaseDao<T,ID extends Serializable> extends IHibernateDao<T, ID>{

}
