package net.diaowen.dwsurvey.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.diaowen.dwsurvey.dao.AnCheckboxDao;
import net.diaowen.dwsurvey.entity.AnCheckbox;
import net.diaowen.dwsurvey.entity.QuRadio;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.common.QuType;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import net.diaowen.common.dao.BaseDaoImpl;
import net.diaowen.dwsurvey.entity.DataCross;
import net.diaowen.dwsurvey.entity.QuCheckbox;

/**
 * 多选题DAO 实现类
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */

@Repository
public class AnCheckboxDaoImpl extends BaseDaoImpl<AnCheckbox, String> implements AnCheckboxDao {

	@Override
	public void findGroupStats(Question question) {
		String sql="select qu_item_id,count(qu_item_id) count from t_an_checkbox where visibility=1 and qu_id=? GROUP BY qu_item_id ;";//构建SQL查询语句
		List<Object[]> list=this.getSession().createSQLQuery(sql).setParameter(1,question.getId()).list();// 执行SQL查询并获取结果
		List<QuCheckbox> quCheckboxs=question.getQuCheckboxs();//得到该问题中的所有多选框

		int count=0;// 初始化计数器
		for (QuCheckbox quCheckbox : quCheckboxs) {//遍历每个多选框
			String quCheckboxId=quCheckbox.getId();
			for (Object[] objects : list) {//遍历SQL查询结果，统计每个多选框被选择的次数
				if(quCheckboxId.equals(objects[0].toString())){
					int anCount=Integer.parseInt(objects[1].toString());
					count+=anCount;
					quCheckbox.setAnCount(anCount);
					continue;
				}
			}
		}
		question.setAnCount(count);// 设置问题的总选择次数
	}

	@Override
	public List<DataCross> findStatsDataCross(Question rowQuestion,
											  Question colQuestion) {
		List<DataCross> dataCrosses=new ArrayList<>();
		List<QuCheckbox> rowList=rowQuestion.getQuCheckboxs();

		Session session=this.getSession();

//		select yesno_answer,qu_item_id,count(*) from t_an_yesno t1, t_an_radio t2"+
//		"where t1.qu_id='402880e63dd92431013dd9297ecc0000'"+
//		"and t2.qu_id='402881c83dbff250013dbff6b2d50000'"+
//		"and t1.belong_answer_id=t2.belong_answer_id GROUP BY yesno_answer,qu_item_id

		//设置SQL查询参数
		String rowTab=" t_an_checkbox t1 ";
		String colTab="";
		String columnSql="";
		String groupSql="";
		String whereSql=" where  t1.visibility=1 and t2.visibility=1 and t1.qu_id=? "+
				" and t2.qu_id=? "+
				" and t1.belong_answer_id=t2.belong_answer_id GROUP BY ";
		String sql="";
		QuType colQuType=colQuestion.getQuType();


		if(colQuType==QuType.YESNO){//多选题与是非题
			//设置SQL查询参数
			colTab=" t_an_yesno t2 ";
			groupSql=" t1.qu_item_id,t2.yesno_answer ";
			sql="select "+groupSql+",count(*) from "+rowTab+","+colTab+whereSql+groupSql;//构建SQL语句
			List<Object[]> objects=session.createSQLQuery(sql).setParameter(1, rowQuestion.getId()).setParameter(1, colQuestion.getId()).list();//执行SQL查询并获取结果

			List<String> colList=new ArrayList<>();
			colList.add(colQuestion.getYesnoOption().getTrueValue());
			colList.add(colQuestion.getYesnoOption().getFalseValue());


			for (QuCheckbox quCheckbox : rowList) {//遍历rowList中的每个QuCheckbox对象
				DataCross rowDataCross=new DataCross();
				String rowName=quCheckbox.getOptionName();
				String rowQuItemId=quCheckbox.getId();
				rowDataCross.setOptionName(rowName);
				List<DataCross> colDataCrosses=rowDataCross.getColDataCrosss();
				for (String col : colList) {//遍历colList中的每个col对象
					DataCross colDataCross=new DataCross();
					colDataCross.setOptionName(col);
					for (Object[] objs : objects) { //遍历SQL查询返回值，检查是否同时匹配rowQuestion和colQuestion的ID以及列问题的答案

						String anRowQuItemId=objs[0].toString();
						String colYesno_answer=objs[1].toString();
						int objCount=Integer.parseInt(objs[2].toString());

						if(rowQuItemId.equals(anRowQuItemId) && col.equals(colYesno_answer)){
							colDataCross.setCount(objCount);//如果匹配，将计数加到colDataCross中
							break;
						}
					}
					colDataCrosses.add(colDataCross);
				}
				dataCrosses.add(rowDataCross);
			}

		}else if(colQuType==QuType.RADIO  || colQuType==QuType.COMPRADIO){//多选题与单选题
			//设置SQL查询参数
			colTab=" t_an_radio t2 ";
			columnSql=" t1.qu_item_id as quItemId1, t2.qu_item_id as quItemId2 ";
			groupSql=" t1.qu_item_id,t2.qu_item_id ";
			sql="select "+columnSql+",count(*) from "+rowTab+","+colTab+whereSql+groupSql;//构建SQL语句
			List<Object[]> objects=session.createSQLQuery(sql).setParameter(1, rowQuestion.getId()).setParameter(1, colQuestion.getId()).list();//执行SQL查询并获取结果

			List<QuRadio> quRadios=colQuestion.getQuRadios();


			for (QuCheckbox quCheckbox : rowList) {//遍历rowList中的每个QuCheckbox对象
				DataCross rowDataCross=new DataCross();
				String rowName=quCheckbox.getOptionName();
				String rowQuItemId=quCheckbox.getId();
				rowDataCross.setOptionName(rowName);
				List<DataCross> colDataCrosses=rowDataCross.getColDataCrosss();
				for (QuRadio quRadio : quRadios) {//遍历colList中的每个QuRadio对象
					DataCross colDataCross=new DataCross();
					colDataCross.setOptionName(quRadio.getOptionName());
					String quRadioId=quRadio.getId();

					for (Object[] objs : objects) {//遍历SQL查询返回值
						String anRowQuItemId=objs[0].toString();
						String anColQuItemId=objs[1].toString();
						int objCount=Integer.parseInt(objs[2].toString());

						if(rowQuItemId.equals(anRowQuItemId) && quRadioId.equals(anColQuItemId)){
							colDataCross.setCount(objCount);
							break;
						}
					}
					colDataCrosses.add(colDataCross);
				}
				dataCrosses.add(rowDataCross);
			}

		}else if(colQuType==QuType.CHECKBOX || colQuType==QuType.COMPCHECKBOX){//多选题与多选题
			//设置SQL查询参数
			colTab=" t_an_checkbox t2 ";
			columnSql=" t1.qu_item_id as quItemId1, t2.qu_item_id as quItemId2 ";
			groupSql=" t1.qu_item_id,t2.qu_item_id ";
			sql="select "+columnSql+",count(*) from "+rowTab+","+colTab+whereSql+groupSql;//构建SQL语句

			List<Object[]> objects=session.createSQLQuery(sql).setParameter(1, rowQuestion.getId()).setParameter(1, colQuestion.getId()).list();//执行SQL查询并获取结果

			List<QuCheckbox> quCheckboxs=colQuestion.getQuCheckboxs();


			for (QuCheckbox rowQuCheckbox : rowList) {//遍历rowList中的每个QuCheckbox对象
				DataCross rowDataCross=new DataCross();
				String rowName=rowQuCheckbox.getOptionName();
				String rowQuItemId=rowQuCheckbox.getId();
				rowDataCross.setOptionName(rowName);
				List<DataCross> colDataCrosses=rowDataCross.getColDataCrosss();

				for (QuCheckbox quCheckbox : quCheckboxs) {//遍历colList中的每个QuCheckbox对象
					DataCross colDataCross=new DataCross();
					colDataCross.setOptionName(quCheckbox.getOptionName());
					String colQuCheckboxId=quCheckbox.getId();
					for (Object[] objs : objects) {//遍历SQL查询返回值
						String anRowQuItemId=objs[0].toString();
						String anColQuItemId=objs[1].toString();
						int objCount=Integer.parseInt(objs[2].toString());

						if(rowQuItemId.equals(anRowQuItemId) && colQuCheckboxId.equals(anColQuItemId)){
							colDataCross.setCount(objCount);
							break;
						}
					}
					colDataCrosses.add(colDataCross);
				}
				dataCrosses.add(rowDataCross);
			}
		}
		return dataCrosses;
	}

	@Override
	public List<DataCross> findStatsDataChart(Question question) {
		List<DataCross> crosses=new ArrayList<>();
		String sql="select qu_item_id,count(*) from t_an_checkbox where  visibility=1 and qu_id=? GROUP BY qu_item_id";//构建SQL语句

		String quId=question.getId();
		List<Object[]> list=this.getSession().createSQLQuery(sql).setParameter(1, quId).list();//执行SQL查询并获取结果

		List<QuCheckbox> quCheckboxs=question.getQuCheckboxs();//得到该问题中的所有多选框
		for (QuCheckbox quCheckbox : quCheckboxs) {//遍历每个QuCheckbox(多选框)对象
			String quItemId=quCheckbox.getId();
			String optionName=quCheckbox.getOptionName();
			DataCross dataCross=new DataCross();
			dataCross.setOptionName(optionName);
			for (Object[] objects : list) {//遍历SQL查询结果，统计每个多选框被选择的次数
				String anQuItemId=objects[0].toString();
				int count=Integer.parseInt(objects[1].toString());
				if(quItemId.equals(anQuItemId)){
					dataCross.setCount(count);
					break;
				}
			}
			crosses.add(dataCross);
		}
		return crosses;
	}

}
