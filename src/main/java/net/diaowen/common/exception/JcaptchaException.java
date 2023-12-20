package net.diaowen.common.exception;

import java.io.IOException;
/**
 * IOException的子类，用于表示JcaptchaController类异常。
 */
public class JcaptchaException extends IOException {
    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public JcaptchaException(String message) {
        super(message);
    }
}
