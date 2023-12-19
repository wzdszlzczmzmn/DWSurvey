/**
 * Copyright (c) 2005-2011 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: Fixtures.java 1593 2011-05-11 10:37:12Z calvinxiu $
 */
package net.diaowen.common.utils;

/**
 * 异常处理工具类，提供将已检查异常转换为未检查异常的功能，就是转换成RuntimeException异常
 */
public class ExceptionUtils {
	private ExceptionUtils(){

	}

	/**
	 * 将CheckedException转换为UnCheckedException.
	 * @param e 待转换的异常
	 * @return 转换后的异常
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			// 检查传入的异常是否已经是 RuntimeException 类型，若是则直接返回
			return (RuntimeException) e;
		}
		// 若不是 RuntimeException 类型，则将其包装成 RuntimeException 并返回
		return new RuntimeException(e.getMessage(), e);
	}
}
