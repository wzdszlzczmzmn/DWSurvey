package net.diaowen.dwsurvey.service;

import java.util.List;

import net.diaowen.common.service.BaseService;
import net.diaowen.dwsurvey.entity.QuMultiFillblank;

/**
 * 多项填空题 填空项业务 接口
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public interface QuMultiFillblankManager  extends BaseService<QuMultiFillblank, String>{

	/**
	 * 根据题目ID查找多项填空题填空项
	 * @param quId 题目ID
	 * @return 返回多项填空题填空项列表
	 */
	public List<QuMultiFillblank> findByQuId(String quId);

	/**
	 * 更新多项填空题的填空项内容
	 * @param quId 题目ID
	 * @param quItemId 要更新的填空项ID（如果为空或空字符串，则表示新增选项）
	 * @param optionName 新的填空项内容
	 * @return 返回更新后的 QuMultiFillblank 对象
	 */
	public QuMultiFillblank upOptionName(String quId, String quItemId, String optionName);

	/**
	 * 保存多项填空题的填空项列表
	 * @param quId 题目ID
	 * @param quMultiFillblanks 要保存的填空项列表
	 * @return 返回保存后的填空项列表
	 */
	public List<QuMultiFillblank> saveManyOptions(String quId, List<QuMultiFillblank> quMultiFillblanks);

	/**
	 * 删除多项填空题的填空项
	 * @param quItemId 填空项ID
	 */
	public void ajaxDelete(String quItemId);

	/**
	 *获取和保存 QuMultiFillblank 对象
	 *
	 * @param quItemId 填空项ID
	 */
	public void saveAttr(String quItemId);
}
