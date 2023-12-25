package net.diaowen.common.base.entity;

/**
 *
 * @author KeYuan
 * @date 2013下午10:01:50
 *
 */
public class TrackerConstants {

	/**
	 * 无参构造函数
	 * @throws IllegalStateException
	 */
	private TrackerConstants() {
		throw new IllegalStateException("Utility class");
	}
	public static final String OPERATION_ADD = "add";
	public static final String OPERATION_DELETE = "delete";
	public static final String OPERATION_UPDATE = "update";
	public static final String OPERATION_SAVE = "save";
}
