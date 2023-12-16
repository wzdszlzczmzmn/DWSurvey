package net.diaowen.dwsurvey.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.diaowen.common.base.entity.User;
import net.diaowen.common.base.service.AccountManager;
import net.diaowen.dwsurvey.dao.QuestionDao;
import net.diaowen.common.QuType;
import net.diaowen.dwsurvey.service.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.diaowen.common.service.BaseServiceImpl;
import net.diaowen.common.utils.ReflectionUtils;
import net.diaowen.dwsurvey.entity.QuCheckbox;
import net.diaowen.dwsurvey.entity.QuMultiFillblank;
import net.diaowen.dwsurvey.entity.QuOrderby;
import net.diaowen.dwsurvey.entity.QuRadio;
import net.diaowen.dwsurvey.entity.QuScore;
import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.entity.QuestionLogic;
import net.diaowen.dwsurvey.entity.SurveyDirectory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * 基础题
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Service("questionManager")
public class QuestionManagerImpl extends BaseServiceImpl<Question, String> implements QuestionManager{

	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private QuCheckboxManager quCheckboxManager;
	@Autowired
	private QuRadioManager quRadioManager;
	@Autowired
	private QuMultiFillblankManager quMultiFillblankManager;
	@Autowired
	private QuScoreManager quScoreManager;
	@Autowired
	private QuOrderbyManager quOrderbyManager;
	@Autowired
	private QuestionLogicManager questionLogicManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private SurveyDirectoryManager surveyDirectoryManager;

	@Override
	public void setBaseDao() {
		this.baseDao=questionDao;
	}


	/**
	 * 所有修改，新增题的入口 方法
	 * @param question
	 */
	@Transactional
	@Override
	public void save(Question question){
		User user=accountManager.getCurUser(); // 拿到当前登陆用户
		if(user!=null){
			// question 所属的问卷
			SurveyDirectory survey = surveyDirectoryManager.getSurvey(question.getBelongId());
			// 判断该问卷是否属于当前用户
			if(user.getId().equals(survey.getUserId())){
				String uuid=question.getId();
				if(uuid==null || "".equals(uuid)){
					question.setId(null);
				}
//				question.setUserUuid(user.getId());
				// 新增的 question 的 Id 在保存到数据库时由 JPA 生成
				questionDao.save(question);
			}
		}
	}


	/**************************************************************************/
	/**
	 * 依据条件得到符合条件的题列表，不包含选项信息   用于列表显示
	 * @param belongId 问卷或题库 Id
	 * @param tag  1题库  2问卷
	 * @return 该问卷或题库下的所有题目（不包括题目的选项信息）
	 */
	public List<Question> find(String belongId,String tag){
		/*Page<Question> page=new Page<Question>();
		page.setOrderBy("orderById");
		page.setOrderDir("asc");

		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_belongId", belongId));
		filters.add(new PropertyFilter("EQI_tag", tag));
		filters.add(new PropertyFilter("NEI_quTag", "3"));
		return findAll(page, filters);*/

		CriteriaBuilder criteriaBuilder=questionDao.getSession().getCriteriaBuilder();
		// 创建一个 Question 的查询
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Question.class);
		Root root = criteriaQuery.from(Question.class);
		criteriaQuery.select(root);
		// 设置查询条件
		Predicate eqQuId = criteriaBuilder.equal(root.get("belongId"),belongId);
		Predicate eqTag = criteriaBuilder.equal(root.get("tag"),tag);
		// 过滤不是大题下面的小题
		Predicate eqQuTag = criteriaBuilder.notEqual(root.get("quTag"),3);
		criteriaQuery.where(eqQuId,eqTag,eqQuTag);
		// 按问题的序号升序排列
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));
		return questionDao.findAll(criteriaQuery);

	}

	/**
	 * 查出指定条件下的所有题，及每一题内容的选项   用于展示试卷,如预览,答卷,查看
	 * @param belongId 问卷或题库 Id
	 * @param tag
	 * @return 该问卷或题库下所有的 Question
	 */
	public List<Question> findDetails(String belongId,String tag){
		List<Question> questions=find(belongId, tag);
		for (Question question : questions) {
			getQuestionOption(question);
		}
		return questions;
	}

	/**
	 * 得到某一题下面的选项，包含大题下面的小题
	 * @param question
	 */
	public void getQuestionOption(Question question) {
		String quId=question.getId();
		QuType quType=question.getQuType(); // 问题类型
		if(quType==QuType.RADIO || quType==QuType.COMPRADIO){  // 单选或复合单选题
			question.setQuRadios(quRadioManager.findByQuId(quId));
		}else if(quType==QuType.CHECKBOX || quType==QuType.COMPCHECKBOX){ // 多选或复合多选
			question.setQuCheckboxs(quCheckboxManager.findByQuId(quId));
		}else if(quType==QuType.MULTIFILLBLANK){ // 多项填空题
			question.setQuMultiFillblanks(quMultiFillblankManager.findByQuId(quId));
		}else if(quType==QuType.BIGQU){ // 大题
			//根据大题ID，找出所有小题
			String parentQuId=question.getId();
			List<Question> childQuList=findByParentQuId(parentQuId);  // 小题列表
			// 得到每个小题下面的所有选项
			for (Question childQu : childQuList) {
				getQuestionOption(childQu);
			}
			question.setQuestions(childQuList);
			//根据小题的类型，取选项
		}else if(quType==QuType.SCORE){  // 评分题
		 List<QuScore>	quScores=quScoreManager.findByQuId(quId);
		 question.setQuScores(quScores);
		}else if(quType==QuType.ORDERQU){ // 	排序题
			 List<QuOrderby>	quOrderbys=quOrderbyManager.findByQuId(quId);
			 question.setQuOrderbys(quOrderbys);
		}
		// 该问题的逻辑
		List<QuestionLogic> questionLogics=questionLogicManager.findByCkQuId(quId);
		question.setQuestionLogics(questionLogics);
	}

	/**
	 * 根据大题 Id 查找其下所有的小题
	 * @param parentQuId 大题 id
	 * @return 该问题下的所有小题
	 */
	public List<Question> findByParentQuId(String parentQuId){
		/*List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_parentQuUuId", parentQuId));
		return findList(filters);*/

		CriteriaBuilder criteriaBuilder=questionDao.getSession().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Question.class);
		Root root = criteriaQuery.from(Question.class);
		criteriaQuery.select(root);
		// 设置查询条件
		Predicate eqParentQuId = criteriaBuilder.equal(root.get("parentQuUuId"),parentQuId);
		criteriaQuery.where(eqParentQuId);
		// 按问题的序号升序排列
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));
		return questionDao.findAll(criteriaQuery);

	}
	/**
	 * 根据所给的 ID 列表，得到对应的问题列表
	 * @param quIds
	 * @param b  表示是否提出每一题的详细选项信息
	 * @return
	 */
	public List<Question> findByQuIds(String[] quIds, boolean b) {
		List<Question> questions=new ArrayList<Question>();
		// 进行判空处理
		if(quIds==null || quIds.length<=0){
			return questions;
		}
		// 构造 hql 语句
		StringBuffer hqlBuf=new StringBuffer("from Question qu where qu.id in(");
		for (String quId : quIds) {
			hqlBuf.append("'"+quId+"'").append(",");
		}
//		hqlBuf.append("0)");
		String hql=hqlBuf.substring(0, hqlBuf.lastIndexOf(","))+")";
		questions=questionDao.find(hql);
		if(b){
			// 对每个问题，查询其所有的选项
			for (Question question : questions) {
				getQuestionOption(question);
			}
		}
		return questions;
	}

	/**
	 * 批量删除题，及题包含的选项一同删除-真删除。
	 * @param delQuIds
	 */
	@Transactional
	public void deletes(String[] delQuIds) {
		if(delQuIds!=null){
			for (String quId : delQuIds) {
				// 添加删除操作
				this.delete(quId);
			}
		}
	}

	/**
	 *	根据 id 删除对应问题
	 * @param quId
	 */
	@Transactional
	public void delete(String quId){
		if(quId!=null && !"".equals(quId)){
			// 根据 id 拿到相应问题
			Question question=get(quId);
			//同时删除掉相应的选项
			if(question!=null){
				QuType quType=question.getQuType();
				String belongId = question.getBelongId();
				int orderById= question.getOrderById();
				questionDao.delete(question);
				//删除题目后，需要更新排序号比该问题排序号大的问题的排序号
				questionDao.quOrderByIdDel1(belongId,orderById);
			}
		}
	}

	/**
	 * 问题排序--交换两个问题的位置
	 * @param prevId 前一个问题的 id
	 * @param nextId 后一个问题的 id
	 */
	@Transactional
	public boolean upsort(String prevId, String nextId) {
		// 判空处理
		if(prevId!=null && !"".equals(prevId) && nextId!=null && !"".equals(nextId)){
			Question prevQuestion=get(prevId);
			Question nextQuestion=get(nextId);
			int prevNum=prevQuestion.getOrderById();
			int nextNum=nextQuestion.getOrderById();

			prevQuestion.setOrderById(nextNum);
			nextQuestion.setOrderById(prevNum);

			save(prevQuestion);
			save(nextQuestion);
			return true;
		}
		return false;
	}

	// 根据 Question ID 查询 Question
	public Question findUnById(String id){
		return questionDao.findUniqueBy("id", id);
	}

	// 根据问卷查询其所有的 Question
	public List<Question> findByparentQuId(String parentQuUuId){
		/*List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQS_parentQuUuId", parentQuUuId));
		return findList(filters);*/
		CriteriaBuilder criteriaBuilder=questionDao.getSession().getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Question.class);
		Root root = criteriaQuery.from(Question.class);
		criteriaQuery.select(root);
		// 设置查询条件
		Predicate eqParentQuId = criteriaBuilder.equal(root.get("parentQuUuId"),parentQuUuId);
		criteriaQuery.where(eqParentQuId);
		// 按问题的序号升序排列
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("orderById")));
		return questionDao.findAll(criteriaQuery);
	}

	/**
	 * 将 questions 中的题目保存到问卷或题库中
	 * @param belongId 问卷或题库 ID
	 * @param tag 1题库中的题   2问卷中的题
	 * @param questions
	 */
	public void saveBySurvey(String belongId,int tag, List<Question> questions) {
		for (Question question : questions) {
			copyQu(belongId, tag, question);
		}
	}
	/**
	 * 保存选中的题目 即从题库或从其它试卷中的题
	 */
	@Transactional
	public void saveChangeQu(String belongId,int tag, String[] quIds) {
		for (String quId : quIds) {
			Question changeQuestion=findUnById(quId);
			copyQu(belongId, tag, changeQuestion);
		}
	}

	/**
	 * 复制问题
	 *
	 * @param belongId 问卷或题库 id
	 * @param tag 题目标记，说明是题库中的题还是问卷中的题
	 * @param changeQuestion
	 */
	private void copyQu(String belongId, int tag, Question changeQuestion) {
		String quId=changeQuestion.getId();
		if(changeQuestion.getQuType()==QuType.BIGQU){ // 处理大题
			Question question=new Question();
			// 将 changeQuestion 的所有属性复制到 question
			ReflectionUtils.copyAttr(changeQuestion,question);
			//设置相关要改变的值
			question.setId(null);
			question.setBelongId(belongId);
			question.setCreateDate(new Date());
			question.setTag(tag);
			question.setQuTag(2); // 设置题目类型为大题
			question.setCopyFromId(quId);

			// 处理大题下面的小题，每个题都要复制
			List<Question> changeChildQuestions=findByparentQuId(quId);
			List<Question> qulits=new ArrayList<Question>();
			for (Question changeQu : changeChildQuestions) {
				Question question2=new Question();
				// 复制所有属性
				ReflectionUtils.copyAttr(changeQu,question2);
				//设置相关要改变的值
				question2.setId(null);
				question2.setBelongId(belongId);
				question2.setCreateDate(new Date());
				question2.setTag(tag);
				question2.setQuTag(3); // 设置题目类型为大题下面的小题
				question2.setCopyFromId(changeQu.getId());

				getQuestionOption(changeQu);
				copyItems(belongId,changeQu, question2);

				qulits.add(question2);
			}
			question.setQuestions(qulits);
			save(question);
		}else{ // 不是大题
			copyroot(belongId, tag, changeQuestion);
		}
	}

	/**
	 *
	 * @param belongId 所属问卷或题库 id
	 * @param tag 题目标记
	 * @param changeQuestion
	 */
	private void copyroot(String belongId,Integer tag, Question changeQuestion) {
		//拷贝先中的问题属性值到新对象中
		Question question=new Question();
		ReflectionUtils.copyAttr(changeQuestion,question);
		//设置相关要改变的值
		question.setId(null);
		question.setBelongId(belongId);
		question.setCreateDate(new Date());
		question.setTag(tag);
		question.setCopyFromId(changeQuestion.getId());

		getQuestionOption(changeQuestion);
		copyItems(belongId,changeQuestion, question);
		save(question);
	}

	/**
	 * 复制问题下的每个选项
	 *
	 * @param quBankUuid unused
	 * @param changeQuestion 要复制的问题
	 * @param question
	 */
	private void copyItems(String quBankUuid,Question changeQuestion, Question question) {
		QuType quType=changeQuestion.getQuType();
		// 单选题和复合单选题
		if(quType==QuType.RADIO || quType==QuType.COMPRADIO){
			List<QuRadio> changeQuRadios=changeQuestion.getQuRadios(); // 单选题选项
			List<QuRadio> quRadios=new ArrayList<QuRadio>();
			// 复制每个选项
			for (QuRadio changeQuRadio : changeQuRadios) {
				QuRadio quRadio=new QuRadio();
				ReflectionUtils.copyAttr(changeQuRadio,quRadio);
				quRadio.setId(null);
				quRadios.add(quRadio);
			}
			question.setQuRadios(quRadios);
		}else if(quType==QuType.CHECKBOX || quType==QuType.COMPCHECKBOX){ // 多选题和复合多选题
			List<QuCheckbox> changeQuCheckboxs=changeQuestion.getQuCheckboxs();
			List<QuCheckbox> quCheckboxs=new ArrayList<QuCheckbox>();
			// 复制每个选项
			for (QuCheckbox changeQuCheckbox : changeQuCheckboxs) {
				QuCheckbox quCheckbox=new QuCheckbox();
				ReflectionUtils.copyAttr(changeQuCheckbox,quCheckbox); // 复制属性
				quCheckbox.setId(null);
				quCheckboxs.add(quCheckbox);
			}
			question.setQuCheckboxs(quCheckboxs);
		}else if(quType==QuType.MULTIFILLBLANK){ // 多项填空题
			List<QuMultiFillblank> changeQuDFillbanks=changeQuestion.getQuMultiFillblanks();
			List<QuMultiFillblank> quDFillbanks=new ArrayList<QuMultiFillblank>();
			for (QuMultiFillblank changeQuDFillbank : changeQuDFillbanks) {
				QuMultiFillblank quDFillbank=new QuMultiFillblank();
				ReflectionUtils.copyAttr(changeQuDFillbank,quDFillbank);
				quDFillbank.setId(null);
				quDFillbanks.add(quDFillbank);
			}
			question.setQuMultiFillblanks(quDFillbanks);
		}else if(quType==QuType.SCORE){
			//评分
			List<QuScore> changeQuScores=changeQuestion.getQuScores();
			List<QuScore> quScores=new ArrayList<QuScore>();
			for (QuScore changeQuScore : changeQuScores) {
				QuScore quScore=new QuScore();
				ReflectionUtils.copyAttr(changeQuScore, quScore);
				quScore.setId(null);
				quScores.add(quScore);
			}
			question.setQuScores(quScores);
		}else if(quType==QuType.ORDERQU) { // 排序题
			List<QuOrderby> changeQuOrderbys=changeQuestion.getQuOrderbys();
			List<QuOrderby> quOrderbyList=new ArrayList<QuOrderby>();
			for (QuOrderby changeQuOrder : changeQuOrderbys) {
				QuOrderby quOrderby=new QuOrderby();
				ReflectionUtils.copyAttr(changeQuOrder, quOrderby);
				quOrderby.setId(null);
				quOrderbyList.add(quOrderby);
			}
			question.setQuOrderbys(quOrderbyList);
		}

	}


	/**
	 * 查询问卷 survey 的题目
	 * @param survey
	 * @return
	 */
	@Override
	public List<Question> findStatsRowVarQus(SurveyDirectory survey) {
		Criterion criterion1=Restrictions.eq("belongId", survey.getId());
		Criterion criterion2=Restrictions.eq("tag", 2);

//		Criterion criterion31=Restrictions.ne("quType", QuType.FILLBLANK);
//		Criterion criterion32=Restrictions.ne("quType", QuType.MULTIFILLBLANK);
//		Criterion criterion33=Restrictions.ne("quType", QuType.ANSWER);
//
////		Criterion criterion3=Restrictions.or(criterion31, criterion32);
//		//where s=2 and (fds !=1 or fds!=2 )
//		return questionDao.find(criterion1,criterion2,criterion31,criterion32,criterion33);

		// 排除所有填空题、多项填空题、多行填空题、多选题、矩阵填空题、矩阵单选题、枚举题、排序题、评分题
		Criterion criterion31=Restrictions.ne("quType", QuType.FILLBLANK);
		Criterion criterion32=Restrictions.ne("quType", QuType.MULTIFILLBLANK);
		Criterion criterion33=Restrictions.ne("quType", QuType.ANSWER);
		Criterion criterion34=Restrictions.ne("quType", QuType.CHENCHECKBOX);
		Criterion criterion35=Restrictions.ne("quType", QuType.CHENFBK);
		Criterion criterion36=Restrictions.ne("quType", QuType.CHENRADIO);
		Criterion criterion37=Restrictions.ne("quType", QuType.ENUMQU);
		Criterion criterion38=Restrictions.ne("quType", QuType.ORDERQU);
		Criterion criterion39=Restrictions.ne("quType", QuType.SCORE);

		return questionDao.find(criterion1,criterion2,criterion31,criterion32,criterion33,criterion34,criterion35,criterion36,criterion37,criterion38,criterion39);
//		return null;
	}


	/**
	 * 查询问卷 survey 的题目
	 * @param survey 问卷
	 * @return
	 */
	@Override
	public List<Question> findStatsColVarQus(SurveyDirectory survey) {
		Criterion criterion1=Restrictions.eq("belongId", survey.getId());
		// 问卷中的题
		Criterion criterion2=Restrictions.eq("tag", 2);

		// 排除所有填空题、多项填空题、多行填空题、多选题、矩阵填空题、矩阵单选题、枚举题、排序题、评分题
		Criterion criterion31=Restrictions.ne("quType", QuType.FILLBLANK);
		Criterion criterion32=Restrictions.ne("quType", QuType.MULTIFILLBLANK);
		Criterion criterion33=Restrictions.ne("quType", QuType.ANSWER);
		Criterion criterion34=Restrictions.ne("quType", QuType.CHENCHECKBOX);
		Criterion criterion35=Restrictions.ne("quType", QuType.CHENFBK);
		Criterion criterion36=Restrictions.ne("quType", QuType.CHENRADIO);
		Criterion criterion37=Restrictions.ne("quType", QuType.ENUMQU);
		Criterion criterion38=Restrictions.ne("quType", QuType.ORDERQU);
		Criterion criterion39=Restrictions.ne("quType", QuType.SCORE);

		return questionDao.find(criterion1,criterion2,criterion31,criterion32,criterion33,criterion34,criterion35,criterion36,criterion37,criterion38,criterion39);
	}


	/**
	 * 根据问题 id 得到其下的所有选项
	 * @param quId
	 * @return
	 */
	@Override
	public Question getDetail(String quId) {
		Question question=get(quId);
		getQuestionOption(question);
		return question;
	}

	// 更新问题
	@Transactional
	public void update(Question entity){
		questionDao.update(entity);
	}

}
