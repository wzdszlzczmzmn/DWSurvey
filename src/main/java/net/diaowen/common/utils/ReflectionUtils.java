/**
 * Copyright (c) 2005-2011 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * $Id: ReflectionUtils.java 1504 2011-03-08 14:49:20Z calvinxiu $
 */
package net.diaowen.common.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;

/**
 * 反射工具类.
 *
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 *
 */
public abstract class ReflectionUtils {
	private ReflectionUtils(){

	}
	private static final String ERROR_MESSAGE = "] on target [";
	//CGLIB动态代理类的分隔符，用于获取真实的Class类型。
	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	//反射工具类的日志记录器。
	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * 调用对象的Getter方法。
	 *
	 * @param obj          目标对象
	 * @param propertyName 属性名
	 * @return Getter方法获取的属性值
	 */
	public static Object invokeGetterMethod(Object obj, String propertyName) {
		//根据属性名构建对应的Getter方法名。
		String getterMethodName = "get" + StringUtils.capitalize(propertyName);
		//通过反射调用构建好的Getter方法获取对象的属性值。
		return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * 调用对象的Setter方法。使用value的Class来查找Setter方法。
	 *
	 * @param obj          目标对象
	 * @param propertyName 属性名
	 * @param value        设置的属性值
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
		//调用对象的Setter方法，根据属性名设置属性值。
		invokeSetterMethod(obj, propertyName, value, null);
	}

	/**
	 * 调用Setter方法.
	 *
	 * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
		//获取属性类型，若未提供类型，则使用值的类型。
		Class<?> type = propertyType != null ? propertyType : value.getClass();
		//构造Setter方法名。
		String setterMethodName = "set" + StringUtils.capitalize(propertyName);
		//调用Setter方法设置属性值。
		invokeMethod(obj, setterMethodName, new Class[] { type }, new Object[] { value });
	}

	/**
	 * 获取对象的指定属性值，无论该属性的访问修饰符是private还是protected。
	 * 不通过getter方法访问属性值。
	 *
	 * @param obj       目标对象
	 * @param fieldName 属性名
	 * @return 属性值
	 * @throws IllegalArgumentException 如果在目标对象上找不到指定属性，则抛出该异常
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		// 获取指定名称的可访问字段
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {    // 如果字段不存在
			//抛出找不到字段的异常
			throw new IllegalArgumentException("Could not find field [" + fieldName + ERROR_MESSAGE + obj + "]");
		}

		//初始化
		Object result = null;
		try {
			// 获取字段值
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			// 记录异常信息
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		//返回字段值
		return result;
	}

	/**
	 * 直接设置对象的指定属性值，无论该属性的访问修饰符是private还是protected。
	 * 不通过setter方法设置属性值。
	 *
	 * @param obj       目标对象
	 * @param fieldName 属性名
	 * @param value     属性值
	 * @throws IllegalArgumentException 如果在目标对象上找不到指定属性，则抛出该异常
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		// 获取指定名称的可访问字段
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) { // 如果字段不存在
			//抛出找不到字段的异常
			throw new IllegalArgumentException("Could not find field [" + fieldName + ERROR_MESSAGE + obj + "]");
		}

		try {

			// 设置字段值
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			//记录异常信息
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

	}

	/**
	 * 通过循环向上转型，获取指定对象的DeclaredField，并强制设置为可访问状态。
	 * 如果向上转型到Object仍无法找到指定的字段，则返回null。
	 *
	 * @param obj       目标对象
	 * @param fieldName 字段名称
	 * @return 获取到的字段对象，如果找不到，则返回null
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		// 确保目标对象不为空
		AssertUtils.notNull(obj, "object不能为空");
		// 确保字段名称不为空
		AssertUtils.hasText(fieldName, "fieldName");
		// 从目标对象的类开始，向上循环查找字段
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				// 尝试获取指定名称的字段
				Field field = superClass.getDeclaredField(fieldName);
				// 设置字段为可访问状态
				field.setAccessible(true);
				// 返回获取到的字段对象
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		// 如果找不到字段，则返回null
		return null;
	}

	/**
	 * 获取被CGLIB AOP处理过的对象的实际Class类型。
	 *
	 * @param clazz 目标Class对象
	 * @return 如果目标对象被CGLIB AOP处理过，则返回其真实的父类类型；否则返回原始的Class类型
	 */
	public static Class<?> getUserClass(Class<?> clazz) {
		// 如果目标Class不为空且包含CGLIB的类分隔符
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			// 获取目标Class的父类
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				// 如果父类不为空且不是Object类，则返回父类类型（即获取真实的Class类型）
				return superClass;
			}
		}
		// 如果没有被CGLIB AOP处理过，则直接返回原始的Class类型
		return clazz;
	}

	/**
	 * 以忽略private/protected修饰符的方式直接调用对象方法。
	 * 适用于仅需一次性调用的情况。
	 *
	 * @param obj             目标对象
	 * @param methodName      方法名
	 * @param parameterTypes  方法参数类型数组
	 * @param args            方法参数数组
	 * @return 调用方法后的返回值
	 * @throws IllegalArgumentException 当目标对象上未找到指定方法时抛出异常
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		// 获取可访问的方法
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {// 如果未找到指定方法
			// 抛出异常
			throw new IllegalArgumentException("Could not find method [" + methodName + ERROR_MESSAGE + obj + "]");
		}

		try {
			// 调用目标方法并返回结果
			return method.invoke(obj, args);
		} catch (Exception e) {
			// 将反射调用异常转换为未检查异常并抛出
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod, 并强制设置为可访问。
	 * 如果向上转型到Object仍无法找到指定方法，则返回null。
	 *
	 * 用于需要多次调用方法的情况。首先使用此函数获取Method，然后调用Method.invoke(Object obj, Object... args)。
	 *
	 * @param obj             目标对象
	 * @param methodName      方法名
	 * @param parameterTypes  方法参数类型数组
	 * @return 可访问的方法对象，如果未找到则返回null
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		//目标对象不能为空
		AssertUtils.notNull(obj, "object不能为空");
		// 从目标对象的类开始向上遍历父类，直到Object类为止
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				//获取声明的方法
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
				// 设置方法为可访问状态
				method.setAccessible(true);
				// 返回可访问的方法对象
				return method;

			} catch (NoSuchMethodException e) {//NOSONAR
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 获取泛型参数的实际类型，通过反射获取指定类定义中声明的父类的泛型参数的类型。
	 * 如无法找到，返回Object.class。
	 *
	 * 例如，对于类声明：public UserDao extends HibernateDao<User, Long>
	 *
	 * @param clazz 目标类
	 * @param index 泛型参数的索引，从0开始计数
	 * @return 索引处的泛型参数类型，如果无法确定则返回Object.class
	 */
	public static Class getSuperClassGenricType(final Class clazz, final int index) {
		// 获取泛型超类的Type对象
		Type genType = clazz.getGenericSuperclass();

		// 如果泛型超类不是ParameterizedType类型
		if (!(genType instanceof ParameterizedType)) {
			// 记录警告日志，说明类的超类不是ParameterizedType类型
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			// 返回object.class
			return Object.class;
		}

		// 获取泛型类型参数的数组
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		// 如果索引超出范围，返回Object.class
		if (index >= params.length || index < 0) {
			//记录警告日志
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			// 返回Object.class
			return Object.class;
		}
		// 如果参数不是Class类型，返回Object.class
		if (!(params[index] instanceof Class)) {
			//记录警告日志
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			//返回Object.class
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 *
	 * @param e 要转换的异常对象
	 * @return 转换后的异常对象
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			// 如果抛出的是IllegalAccessException、IllegalArgumentException或者NoSuchMethodException，则抛出IllegalArgumentException
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			// 如果抛出的是InvocationTargetException，则抛出RuntimeException
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			// 如果抛出的是RuntimeException，则直接返回
			return (RuntimeException) e;
		}
		// 如果抛出的是其他异常，则抛出RuntimeException
		return new RuntimeException("Unexpected Checked Exception.", e);
	}


	/**
	 * 拷贝属性值
	 * @param fromObj 拷贝源对象
	 * @param copyObj 拷贝目标对象
	 * @param <T> 对象类型
	 */
	public static <T> void copyAttr(T fromObj,T copyObj){
		//获取fromObj的属性
		Field[] fileds=fromObj.getClass().getDeclaredFields();
		//遍历属性
		for (Field field : fileds) {
			try {
				//设置属性可访问
				field.setAccessible(true);
				//获取属性值
				Object value = field.get(fromObj);
				//设置copyObj的属性值
				field.set(copyObj, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
