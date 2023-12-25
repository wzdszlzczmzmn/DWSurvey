package net.diaowen.common.dao;

import java.io.Serializable;

/**
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 * BaseDaoImpl类，用于实现BaseDao接口。
 *
 * @param <T>  实体类型
 * @param <ID> 实体ID类型
 */
public class BaseDaoImpl<T,ID extends Serializable> extends HibernateDao<T, ID> implements BaseDao<T, ID>{


}
