package net.diaowen.dwsurvey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.diaowen.common.base.entity.IdEntity;

/**
 * 答卷  多项填空题保存表
 * @author keyuan
 * @date 2012-10-21下午9:26:43
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Entity
@Table(name="t_an_dfillblank")
public class AnDFillblank extends IdEntity{

	/**
	 * 所属问卷ID
	 */
	private String belongId;

	/**
	 * 对应的答卷信息表ID
	 */
	private String belongAnswerId;

	/**
	 * 题目ID
	 */
	private String quId;

	/**
	 * 对应的填空项ID
	 */
	private String quItemId;

	/**
	 * 答案
	 */
	private String answer;

	/**
	 * 可见性，默认值为1（可见）
	 */
	private Integer visibility=1;

	/**
	 * 空构造函数
	 */
	public AnDFillblank(){

	}

	/**
	 * 带参数的构造函数
	 * @param surveyId 所属问卷ID
	 * @param surveyAnswerId 对应的答卷信息表ID
	 * @param quId 题目 ID
	 * @param quItemId 对应的填空项ID
	 * @param answerValue 答案
	 */
	public AnDFillblank(String surveyId, String surveyAnswerId, String quId,
			String quItemId, String answerValue) {
		this.belongId=surveyId;
		this.belongAnswerId=surveyAnswerId;
		this.quId=quId;
		this.quItemId=quItemId;
		this.answer=answerValue;
	}


	/**
	 * 获取所属问卷ID
	 * @return 所属问卷ID
	 */
	public String getBelongId() {
		return belongId;
	}

	/**
	 * 设置所属问卷ID
	 * @param belongId 所属问卷ID
	 */
	public void setBelongId(String belongId) {
		this.belongId = belongId;
	}

	/**
	 * 获取对应的答卷信息表ID
	 * @return 对应的答卷信息表ID
	 */
	public String getBelongAnswerId() {
		return belongAnswerId;
	}

	/**
	 * 设置对应的答卷信息表ID
	 * @param belongAnswerId 对应的答卷信息表ID
	 */
	public void setBelongAnswerId(String belongAnswerId) {
		this.belongAnswerId = belongAnswerId;
	}

	/**
	 * 获取题目ID
	 * @return 题目ID
	 */
	public String getQuId() {
		return quId;
	}

	/**
	 * 设置题目ID
	 * @param quId 题目ID
	 */
	public void setQuId(String quId) {
		this.quId = quId;
	}

	/**
	 * 获取对应的填空项ID
	 * @return 对应的填空项ID
	 */
	public String getQuItemId() {
		return quItemId;
	}

	/**
	 * 设置对应的填空项ID
	 * @param quItemId 对应的填空项ID
	 */
	public void setQuItemId(String quItemId) {
		this.quItemId = quItemId;
	}

	/**
	 * 获取答案
	 * @return 答案
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * 设置答案
	 * @param answer 答案
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * 获取可见性
	 * @return 可见性
	 */
	public Integer getVisibility() {
		return visibility;
	}

	/**
	 * 设置可见性
	 * @param visibility 可见性
	 */
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
}
