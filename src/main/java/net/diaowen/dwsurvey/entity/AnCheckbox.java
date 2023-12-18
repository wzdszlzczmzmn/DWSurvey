package net.diaowen.dwsurvey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.diaowen.common.base.entity.IdEntity;
/**
 * 答卷  多选题保存表
 * @author keyuan(keyuan258@gmail.com)
 * @date 2012-10-21下午9:26:43
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */
@Entity
@Table(name="t_an_checkbox")
public class AnCheckbox extends IdEntity {

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
	 * 对应的选项ID
	 */
	private String quItemId;

	/**
	 * 复合的说明
	 */
	private String otherText;

	/**
	 * 可见性，默认值为1（可见）
	 */
	private Integer visibility=1;

	/**
	 * 空构造函数
	 */
	public AnCheckbox(){

	}
	/**
	 * 带参数的构造函数
	 * @param surveyId 所属问卷ID
	 * @param surveyAnswerId 对应的答卷信息表ID
	 * @param quId 题目ID
	 * @param quItemId 对应的选项ID
	 */
	public AnCheckbox(String surveyId, String surveyAnswerId, String quId,
			String quItemId) {
		this.belongId=surveyId;
		this.belongAnswerId=surveyAnswerId;
		this.quId=quId;
		this.quItemId=quItemId;
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
	 * 获取对应的选项ID
	 * @return 对应的选项ID
	 */
	public String getQuItemId() {
		return quItemId;
	}

	/**
	 * 设置对应的选项ID
	 * @param quItemId 对应的选项ID
	 */
	public void setQuItemId(String quItemId) {
		this.quItemId = quItemId;
	}

	/**
	 * 获取复合的说明
	 * @return 复合的说明
	 */
	public String getOtherText() {
		return otherText;
	}

	/**
	 * 设置复合的说明
	 * @param otherText 复合的说明
	 */
	public void setOtherText(String otherText) {
		this.otherText = otherText;
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
