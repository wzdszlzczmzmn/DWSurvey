package net.diaowen.dwsurvey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.diaowen.common.CheckType;

import net.diaowen.common.base.entity.IdEntity;

/**
 * 多选题 选项表
 * @author keyuan
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Entity
@Table(name="t_qu_checkbox")
public class QuCheckbox extends IdEntity{
	/**
	 * 所属题ID
	 */
	private String quId;

	/**
	 * 选项标题
	 */
	private String optionTitle;

	/**
	 * 选项内容
	 */
	private String optionName;

	/**
	 * 是否带说明  0否  1是
	 */
	private Integer isNote=0;

	/**
	 * 说明的验证方式
	 */
	private CheckType checkType;

	/**
	 * 说明内容是否必填
	 */
	private Integer isRequiredFill=0;

	/**
	 * 排序ID
	 */
	private Integer orderById;

	/**
	 * 可见性，默认值为1（可见）
	 */
	private Integer visibility=1;

	/**
	 * 获取所属题ID
	 * @return 所属题ID
	 */
	public String getQuId() {
		return quId;
	}

	/**
	 * 设置所属题ID
	 * @param quId 所属题ID
	 */
	public void setQuId(String quId) {
		this.quId = quId;
	}

	/**
	 * 获取选项标题
	 * @return 选项标题
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * 设置选项标题
	 * @param optionTitle 选项标题
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * 获取选项内容
	 * @return 选项内容
	 */
	public String getOptionName() {
		return optionName;
	}

	/**
	 * 设置选项内容
	 * @param optionName 选项内容
	 */
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	/**
	 * 获取排序ID
	 * @return 排序ID
	 */
	public Integer getOrderById() {
		return orderById;
	}

	/**
	 * 设置排序ID
	 * @param orderById 排序ID
	 */
	public void setOrderById(Integer orderById) {
		this.orderById = orderById;
	}

	/**
	 * 获取isNote
	 * @return isNote 是否带说明
	 */
	public Integer getIsNote() {
		return isNote;
	}

	/**
	 * 设置isNote
	 * @param isNote 是否带说明
	 */
	public void setIsNote(Integer isNote) {
		this.isNote = isNote;
	}

	/**
	 * 获取说明的验证方式
	 * @return 说明的验证方式
	 */
	public CheckType getCheckType() {
		return checkType;
	}

	/**
	 * 设置说明的验证方式
	 * @param checkType 说明的验证方式
	 */
	public void setCheckType(CheckType checkType) {
		this.checkType = checkType;
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

	/**
	 * 获取isRequiredFill
	 * @return isRequiredFill 内容是否必填
	 */
	public Integer getIsRequiredFill() {
		return isRequiredFill;
	}

	/**
	 * 设置isRequiredFill
	 * @param isRequiredFill 内容是否必填
	 */
	public void setIsRequiredFill(Integer isRequiredFill) {
		this.isRequiredFill = isRequiredFill;
	}


	/**
	 * 选择该选项的次数
	 */
	private Integer anCount;

	@Transient
	/**
	 * 获取选择该选项的次数
	 * @return 选择该选项的次数
	 */
	public Integer getAnCount() {
		return anCount;
	}

	/**
	 * 设置选择该选项的次数
	 * @param anCount 选择该选项的次数
	 */
	public void setAnCount(Integer anCount) {
		this.anCount = anCount;
	}

}
