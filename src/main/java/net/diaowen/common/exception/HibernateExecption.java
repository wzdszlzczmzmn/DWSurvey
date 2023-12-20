package net.diaowen.common.exception;
/**
 * RuntimeException的子类，用于表示HibernateDao类异常。
 */
public class HibernateExecption extends RuntimeException{
    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param cause   异常原因
     */
    public HibernateExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
