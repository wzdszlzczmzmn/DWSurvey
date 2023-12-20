package net.diaowen.common.exception;

/**
 * Service层公用的Exception.
 *
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 *
 * @author keyuan
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1401593546385403720L;
	/**
	 * 无参构造函数
	 */
	public ServiceException() {
		super();
	}
	/**
	 * 构造函数，用于创建ServiceException对象。
	 *
	 * @param message 异常信息
	 */
	public ServiceException(String message) {
		super(message);
	}
	/**
	 * 构造函数，用于创建ServiceException对象。
	 *
	 * @param cause 异常原因
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}
	/**
	 * 构造函数，用于创建ServiceException对象。
	 *
	 * @param message 异常信息
	 * @param cause   异常原因
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
