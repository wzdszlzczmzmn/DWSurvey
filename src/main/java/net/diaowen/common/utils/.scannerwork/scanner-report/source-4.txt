package net.diaowen.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于添加cookie和获取cookie
 * 可以指定添加的cookie的名称、值、生命周期和路径
 */
public class CookieUtils {
	private CookieUtils(){

	}
	/**
	 * 添加cookie
	 * 
	 * @param response
	 * @param name
	 *            Cookie的名称，不能为null
	 * @param value
	 *            Cookie的值，默认值空字符串
	 * @param maxAge
	 * 			  Cookie生命周期，以秒为单位
	 * @param path
	 *            默认值'/'
	 */
	public static void addCookie(HttpServletResponse response, String name,
			String value, Integer maxAge, String path) {
		if (value == null) {
			//如果value没有填写，是null，则使用默认值空字符串
			value = "";
		}
		if (path == null) {
			//如果path没有填写，是null，则使用默认值“/”
			path = "/";
		}

		//创建一个新的cookie实例，并用name和value初始化
		Cookie cookie = new Cookie(name, value);
		//设置cookie的path为输入的path
		cookie.setPath(path);
		if (maxAge != null) {
			//设置maxAge的生命周期
			cookie.setMaxAge(maxAge);
		}
		//将创建的cookie实例加入到HttpServletResponse接口response中
		response.addCookie(cookie);
	}
	
	/**
	 * 设置cookie
	 * 
	 * @param response
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */
	public static void addCookie(HttpServletResponse response, String name,
			String value, int maxAge) {
		//创建一个新的cookie实例，并用name和value初始化
		Cookie cookie = new Cookie(name, value);
		//设置cookie的路径为“/”
		cookie.setPath("/");
		if (maxAge > 0)
			//如果所输入的maxAge大于0，就更新cookie的生命周期为指定的maxAge
			cookie.setMaxAge(maxAge);
		//将创建的cookie实例加入到HttpServletResponse接口response中
		response.addCookie(cookie);
	}


	/**
	 * @param request
	 * @param cookieName
	 * @return 指定的cookie
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		//从HttpServletRequest获取所有cookie
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			//如果没有cookie，就返回null
			return null;
		}

		for (Cookie c : cookies) {
			if (c.getName().equals(cookieName)) {
				//遍历所有cookie，找到指定名称的cookie，并返回它
				return c;
			}
		}

		//如果没有指定名称的cookie，就返回null
		return null;
	}

}
