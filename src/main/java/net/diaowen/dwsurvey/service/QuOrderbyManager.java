package net.diaowen.dwsurvey.service;

import java.util.List;

import net.diaowen.common.service.BaseService;
import net.diaowen.dwsurvey.entity.QuOrderby;

/**
 * 排序题业务 接口
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface QuOrderbyManager extends BaseService<QuOrderby, String>{

	/**
	 * 根据题目ID查找排序题选项
	 * @param id 题目ID
	 * @return 返回排序题选项列表
	 */
	public List<QuOrderby> findByQuId(String id);

	/**
	 * 更新排序题的选项内容
	 * @param quId 题目ID
	 * @param quItemId 要更新的选项ID（如果为空或空字符串，则表示新增选项）
	 * @param optionName 新的选项内容
	 * @return 返回更新后的 QuOrderby 对象
	 */
	public QuOrderby upOptionName(String quId, String quItemId, String optionName);

	/**
	 * 保存排序题选项列表
	 * @param quId 题目ID
	 * @param quOrderbys 要保存的选项列表
	 * @return 返回保存后的选项列表
	 */
	public List<QuOrderby> saveManyOptions(String quId, List<QuOrderby> quOrderbys);

	/**
	 * 删除排序题选项
	 * @param quItemId 选项ID
	 */
	public void ajaxDelete(String quItemId);

	/**
	 * 获取和保存 QuOrderby 对象
	 *
	 * @param quItemId 选项ID
	 */
	public void saveAttr(String quItemId);
}
