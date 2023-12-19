package net.diaowen.dwsurvey.dao.impl;

import java.util.List;

import net.diaowen.dwsurvey.dao.AnDFillblankDao;
import org.springframework.stereotype.Repository;

import net.diaowen.common.dao.BaseDaoImpl;
import net.diaowen.dwsurvey.entity.AnDFillblank;
import net.diaowen.dwsurvey.entity.QuMultiFillblank;
import net.diaowen.dwsurvey.entity.Question;

/**
 * 多项填空DAO 实现类
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */

@Repository
public class AnDFillblankDaoImpl extends BaseDaoImpl<AnDFillblank, String> implements AnDFillblankDao {

	@Override
	public void findGroupStats(Question question) {
		// 定义SQL查询语句，用于统计多项填空题的每个填空项的数量
		String sql="select qu_item_id,count(*) from t_an_dfillblank where  visibility=1 and  qu_id=? group by qu_item_id";

		List<Object[]> list=this.getSession().createSQLQuery(sql).setParameter(1,question.getId()).list();// 执行查询，获取结果
		List<QuMultiFillblank> quMultiFillblanks=question.getQuMultiFillblanks();// 获取question中的QuMultiFillblank列表

		// 遍历QuMultiFillblank列表
		for (QuMultiFillblank quMultiFillblank : quMultiFillblanks) {
			String quMultiFillblankId=quMultiFillblank.getId();
			for (Object[] objects : list) {// 遍历查询结果列表
				// 如果QuMultiFillblank的id与查询结果中的qu_item_id匹配，则设置该QuMultiFillblank的an_count属性
				if(quMultiFillblankId.equals(objects[0].toString())){
					quMultiFillblank.setAnCount(Integer.parseInt(objects[1].toString()));
					continue;
				}
			}
		}
	}

}
