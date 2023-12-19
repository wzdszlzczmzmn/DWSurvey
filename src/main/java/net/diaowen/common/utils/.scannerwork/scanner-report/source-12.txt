package net.diaowen.common.utils.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitAppliction implements ServletContextListener {

	private static String contextPath = null;

	/**
	 * 当前功能暂无需在 Servlet 销毁时执行任何逻辑。
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// 这里留空，因为当前无需执行销毁逻辑
	}

	@Override
	public  void contextInitialized(ServletContextEvent sce) {

		ServletContext servletContext = sce.getServletContext();
		contextPath = servletContext.getContextPath();

	}



}
