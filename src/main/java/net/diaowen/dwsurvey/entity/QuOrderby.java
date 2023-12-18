package net.diaowen.dwsurvey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.diaowen.common.base.entity.IdEntity;

/**
 * 排序题 行选项表
 * @author KeYuan
 * @date 2013下午12:14:26
 *
 */
@Entity
@Table(name="t_qu_orderby")
public class QuOrderby extends IdEntity {

	/**
	 * 所属题ID
	 */
	private String quId;

	/**
	 * 选项标识
	 */
	private String optionTitle;

	/**
	 * 选项内容
	 */
	private String optionName;

	/**
	 * 排序ID
	 */
	private Integer orderById;

	/**
	 * 可见性，默认值为1（可见）
	 */
	private Integer visibility = 1;

	/**
	 * 获取所属题ID
	 *
	 * @return 所属题ID
	 */
	public String getQuId() {
		return quId;
	}

	/**
	 * 设置所属题ID
	 *
	 * @param quId 所属题ID
	 */
	public void setQuId(String quId) {
		this.quId = quId;
	}

	/**
	 * 获取选项标题
	 *
	 * @return 选项标题
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * 设置选项标题
	 *
	 * @param optionTitle 选项标题
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * 获取选项问题
	 *
	 * @return 选项问题
	 */
	public String getOptionName() {
		return optionName;
	}

	/**
	 * 设置选项问题
	 *
	 * @param optionName 选项问题
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
	 * 该选项的所有排序和
	 */
	private int anOrderSum;

	@Transient
	/**
	* 获取该选项的所有排序和
	* @return 该选项的所有排序和
	*/
	public int getAnOrderSum() {
		return anOrderSum;
	}

	/**
	 * 设置该选项的所有排序和
	 * @param anOrderSum 该选项的所有排序和
	 */
	public void setAnOrderSum(int anOrderSum) {
			this.anOrderSum = anOrderSum;
		}


}
