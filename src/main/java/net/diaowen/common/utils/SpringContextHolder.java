package net.diaowen.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 * 这允许在应用程序的任何地方通过静态方法获取ApplicationContext，从而获取Spring容器中的Bean或执行其他Spring操作。
 * 该类实现了ApplicationContextAware接口和DisposableBean接口，可以在ApplicationContext注入时将其存储为静态变量，并在关闭时清理静态变量。
 *
 */
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	/**
	 * 静态变量，用于存储ApplicationContext对象
	 */
	private static ApplicationContext applicationContext = null;

	/**
	 * 静态变量，用于记录日志
	 */
	private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		// 确认上下文已注入
		assertContextInjected();
		// 返回上下文
		return applicationContext;

	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(String name) {
		// 确保上下文已注入
		assertContextInjected();
		// 返回应用上下文中的Bean对象
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		// 确保上下文中已经注入了ApplicationContext对象
		assertContextInjected();
		// 返回ApplicationContext中对应类型的Bean对象
		return applicationContext.getBean(requiredType);

	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clearHolder() {
		logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		applicationContext = null;
	}

	/**
	 * 实现ApplicationContextAware接口, 注入Context到静态变量中.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		/**
		 * 注入ApplicationContext到SpringContextHolder
		 */
		logger.debug("注入ApplicationContext到SpringContextHolder:" + applicationContext);

		/**
		 * 如果SpringContextHolder中的ApplicationContext不为空，则打印警告信息
		 * 并且原有ApplicationContext被覆盖
		 */
		if (SpringContextHolder.applicationContext != null) {
		    logger.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:"
		            + SpringContextHolder.applicationContext);
		}

		// 将传入的ApplicationContext赋值给SpringContextHolder中的ApplicationContext
		SpringContextHolder.applicationContext = applicationContext; //NOSONAR

	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	@Override
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}

	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		AssertUtils.state(applicationContext != null,
				"applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
	}
}
