package net.diaowen.dwsurvey.service;

import java.util.List;

import net.diaowen.common.service.BaseService;
import net.diaowen.dwsurvey.entity.QuCheckbox;

/**
 * 多选题选项业务 接口
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface QuCheckboxManager  extends BaseService<QuCheckbox, String>{

	/**
	 * 根据题目ID查找多选题选项
	 * @param quId 题目ID
	 * @return 返回多选题选项列表
	 */
	public List<QuCheckbox> findByQuId(String quId);

	/**
	 * 更新多选题的选项内容
	 * @param quId 题目ID
	 * @param quItemId 要更新的选项ID（如果为空或空字符串，则表示新增选项）
	 * @param optionName 新的选项内容
	 * @return 返回更新后的 QuCheckbox 对象
	 */
	public QuCheckbox upOptionName(String quId, String quItemId, String optionName);

	/**
	 * 保存多选题选项列表
	 * @param quId 题目ID
	 * @param quCheckboxs 要保存的选项列表
	 * @return 返回保存后的选项列表
	 */
	public List<QuCheckbox> saveManyOptions(String quId, List<QuCheckbox> quCheckboxs);

	/**
	 * 删除多选题选项
	 * @param quItemId 选项ID
	 */
	public void ajaxDelete(String quItemId);

	/**
	 * 获取和保存 QuCheckbox 对象
	 *
	 * @param quItemId 选项ID
	 * @param isNote 是否带说明
	 */
	public void saveAttr(String quItemId, String isNote);

}
