package net.diaowen.dwsurvey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.diaowen.common.base.entity.IdEntity;

/**
 * 答卷  排序题保存表
 * @author KeYuan
 * @date 2013下午8:48:24
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Entity
@Table(name="t_an_order")
public class AnOrder extends IdEntity{
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
	 * 对应的行ID
	 */
	private String quRowId;

	/**
	 * 对应的排序序号
	 */
	private String orderyNum;

	/**
	 * 可见性，默认值为1（可见）
	 */
	private Integer visibility=1;

	/**
	 * 空构造函数
	 */
	public AnOrder(){

	}

	/**
	 * 带参数的构造函数
	 * @param surveyId 所属问卷ID
	 * @param surveyAnswerId 对应的答卷信息表ID
	 * @param quId 题目ID
	 * @param quRowId 对应的行ID
	 * @param orderyNum 对应的排序序号
	 */
	public AnOrder(String surveyId, String surveyAnswerId, String quId,String quRowId,String orderyNum) {
		this.belongId=surveyId;
		this.belongAnswerId=surveyAnswerId;
		this.quId=quId;
		this.quRowId=quRowId;
		this.orderyNum=orderyNum;
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
	 * 获取对应的行ID
	 * @return 对应的行ID
	 */
	public String getQuRowId() {
		return quRowId;
	}

	/**
	 * 设置对应的行ID
	 * @param quRowId 对应的行ID
	 */
	public void setQuRowId(String quRowId) {
		this.quRowId = quRowId;
	}

	/**
	 * 获取对应的排序序号
	 * @return 对应的排序序号
	 */
	public String getOrderyNum() {
		return orderyNum;
	}

	/**
	 * 设置对应的排序序号
	 * @param orderyNum 对应的排序序号
	 */
	public void setOrderyNum(String orderyNum) {
		this.orderyNum = orderyNum;
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
