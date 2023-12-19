/**
 * Copyright (c) 2005-2011 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: Fixtures.java 1593 2011-05-11 10:37:12Z calvinxiu $
 */
package net.diaowen.common.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 *   断言工具，用于在代码中执行断言检查，确保在运行时满足特定的条件，否则抛出异常
 */
public abstract class AssertUtils {
	private AssertUtils(){

	}
	private static final String CHECK_TYPE_NOT_NULL = "Type to check against must not be null";
	/**
	 * 确保布尔表达式为真，如果测试结果为假，则抛出 <code>IllegalArgumentException</code> 异常。
	 *
	 * @param expression 布尔表达式
	 * @throws IllegalArgumentException 如果表达式为 <code>false</code>
	 */
	public static void isTrue(boolean expression) {
		// 调用 isTrue(expression, "[Assertion failed] - this expression must be true") 方法
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	/**
	 * 确保布尔表达式为真，如果测试结果为假，则抛出 <code>IllegalArgumentException</code> 异常。
	 *
	 * @param expression 布尔表达式
	 * @param message 如果断言失败，使用的异常信息
	 * @throws IllegalArgumentException 如果表达式为 <code>false</code>
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			// 如果表达式为假，抛出 IllegalArgumentException 异常，并使用给定的消息
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 确保布尔表达式为真，如果测试结果为假，则抛出给定的 RuntimeException 异常。
	 *
	 * @param expression 布尔表达式
	 * @param throwIfAssertFail 断言失败时要抛出的 RuntimeException
	 */
	public static void isTrue(boolean expression, RuntimeException throwIfAssertFail) {
		if (!expression) {
			// 如果表达式为假，抛出给定的 RuntimeException 异常
			throw throwIfAssertFail;
		}
	}

	/**
	 * 断言实体是 <code>null</code>
	 *
	 * @param object 要断言的实体
	 * @throws IllegalArgumentException 如果实体不是null，抛出的异常
	 */
	public static void isNull(Object object) {
		//调用isNull(object, "[Assertion failed] - the object argument must be null")方法
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	/**
	 * 断言实体是 <code>null</code>
	 *
	 * @param object 要断言的实体
	 * @param message 如果断言失败，使用的异常信息
	 * @throws IllegalArgumentException 如果实体不是null，抛出的异常
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			// 如果实体不是null，抛出 IllegalArgumentException 异常，并使用给定的消息
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言实体是 <code>null</code>
	 *
	 * @param object 要断言的实体
	 * @param throwIfAssertFail 如果断言失败，使用的异常信息
	 */
	public static void isNull(Object object, RuntimeException throwIfAssertFail) {
		if (object != null) {
			//如果实体不是null，抛出throwIfAssertFail异常
			throw throwIfAssertFail;
		}
	}

	/**
	 * 断言实体不是 <code>null</code>
	 *
	 * @param object 要断言的实体
	 * @throws IllegalArgumentException 如果实体是null，抛出的异常
	 */
	public static void notNull(Object object) {
		//调用notNull(object, "[Assertion failed] - this argument is required; it must not be null")方法
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	/**
	 * 断言实体不是 <code>null</code>
	 *
	 * @param object 要断言的实体
	 * @param message 如果断言失败，使用的异常信息
	 * @throws IllegalArgumentException 如果实体是null，抛出的异常
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			//如果实体是null,抛出IllegalArgumentException，并使用给定的信息
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言实体不是 <code>null</code>
	 *
	 * @param object 要断言的实体
	 * @param throwIfAssertFail 如果断言失败，使用的异常信息
	 */
	public static void notNull(Object object, RuntimeException throwIfAssertFail) {
		if (object == null) {
			//如果实体是null，抛出throwIfAssertFail的异常
			throw throwIfAssertFail;
		}
	}

	/**
	 * 断言字符串不是<code>null</code>
	 * @param text 要断言的字符串
	 * @see StringUtils#hasLength 与 StringUtils 类中的 hasLength 方法相关联
	 * @throws IllegalArgumentException 如果断言的字符串为null，抛出IllegalArgumentException异常
	 */
	public static void hasLength(String text) {
		//调用hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty")方法
		hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	/**
	 * 断言字符串不是<code>null</code>
	 * @param text 要断言的字符串
	 * @param message 若断言的字符串为null，抛出的异常信息内容
	 * @see StringUtils#hasLength 与 StringUtils 类中的 hasLength 方法相关联
	 * @throws IllegalArgumentException 如果断言的字符串为null，抛出IllegalArgumentException异常
	 */
	public static void hasLength(String text, String message) {
		if (!StringUtils.hasLength(text)) {
			//如果断言的字符串为null，抛出IllegalArgumentException异常，并使用给定的信息
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言字符串不是<code>null</code>
	 * @param text 要断言的字符串
	 * @param throwIfAssertFail 若断言的字符串为null，使用的异常信息
	 * @see StringUtils#hasLength 与 StringUtils 类中的 hasLength 方法相关联
	 */
	public static void hasLength(String text, RuntimeException throwIfAssertFail) {
		if (!StringUtils.hasLength(text)) {
			//如果断言的字符串为null，抛出throwIfAssertFail的异常信息
			throw throwIfAssertFail;
		}
	}

	/**
	 * 断言字符串是否为有效字符串，不能是<code>null</code>并且包含至少一个非空白字符
	 * @param text 要断言的字符串
	 * @see StringUtils#hasText 与StringUtils的hasText方法相关联
	 * @throws IllegalArgumentException 如果断言的字符串不是有效字符串
	 */
	public static void hasText(String text) {
		//调用hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank")方法
		hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	/**
	 * 断言字符串是否为有效字符串，不能是<code>null</code>并且包含至少一个非空白字符
	 * @param text 要断言的字符串
	 * @param message 若断言的字符串不是有效字符串，抛出的异常信息内容
	 * @see StringUtils#hasText 与StringUtils的hasText方法相关联
	 * @throws IllegalArgumentException 如果断言的字符串不是有效字符串
	 */
	public static void hasText(String text, String message) {
		if (!StringUtils.hasText(text)) {
			//如果断言的字符串不是有效字符串，抛出IllegalArgumentExcept的异常，并使用给定的信息
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言字符串是否为有效字符串，不能是<code>null</code>并且包含至少一个非空白字符
	 * @param text 要断言的字符串
	 * @param throwIfAssertFail 若断言的字符串不是有效字符串，使用的异常信息
	 * @see StringUtils#hasText 与StringUtils的hasText方法相关联
	 */
	public static void hasText(String text, RuntimeException throwIfAssertFail) {
		if (!StringUtils.hasText(text)) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * 断言被搜索的字符串没有包含要搜索的字符串
	 * @param textToSearch 被搜索的字符串
	 * @param substring 要搜索的字符串
	 * @throws IllegalArgumentException 如果被搜索的字符串包含要搜索的字符串，抛出IllegalArgumentException异常信息
	 */
	public static void doesNotContain(String textToSearch, String substring) {
		//调用		doesNotContain(textToSearch, substring,
		//				"[Assertion failed] - this String argument must not contain the substring [" + substring + "]")方法
		doesNotContain(textToSearch, substring,
				"[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
	}

	/**
	 * 断言被搜索的字符串没有包含要搜索的字符串
	 * @param textToSearch 被搜索的字符串
	 * @param substring 要搜索的字符串
	 * @param message 若被搜索的字符串包含要搜索的字符串，所抛出的异常信息
	 * @throws IllegalArgumentException 如果被搜索的字符串包含要搜索的字符串，抛出IllegalArgumentException异常信息
	 */
	public static void doesNotContain(String textToSearch, String substring, String message) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
				&& textToSearch.indexOf(substring) != -1) {
			//若被搜索的字符串包含要搜索的字符串，抛出IllegalArgumentException异常，并使用给定的信息
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言被搜索的字符串没有包含要搜索的字符串
	 * @param textToSearch 被搜索的字符串
	 * @param substring 要搜索的字符串
	 * @param throwIfAssertFail 若被搜索的字符串包含要搜索的字符串，所抛出的异常
	 */
	public static void doesNotContain(String textToSearch, String substring, RuntimeException throwIfAssertFail) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
				&& textToSearch.indexOf(substring) != -1) {
			//若被搜索的字符串包含要搜索的字符串，抛出throwIfAssertFail异常
			throw throwIfAssertFail;
		}
	}


	/**
	 * 断言数组不是空，即数组不能是<code>null</code>，并且至少有1个元素
	 * @param array 要断言的数组
	 * @throws IllegalArgumentException 若断言的数组为空或者没有元素，抛出IllegalArgumentException异常
	 */
	public static void notEmpty(Object[] array) {
		//调用notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element")方法
		notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	/**
	 * 断言数组不是空，即数组不能是<code>null</code>，并且至少有1个元素
	 * @param array 要断言的数组
	 * @param message 若断言的数组为空或者没有元素，所抛出的异常信息
	 * @throws IllegalArgumentException 若断言的数组为空或者没有元素，抛出IllegalArgumentException异常
	 */
	public static void notEmpty(Object[] array, String message) {
		if (ObjectUtils.isEmpty(array)) {
			//若断言的数组为空或者没有元素，抛出IllegalArgumentException异常，并使用给定的信息
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言数组不是空，即数组不能是<code>null</code>，并且至少有1个元素
	 * @param array 要断言的数组
	 * @param throwIfAssertFail 若断言的数组为空或者没有元素，所抛出的异常
	 */
	public static void notEmpty(Object[] array, RuntimeException throwIfAssertFail) {
		if (ObjectUtils.isEmpty(array)) {
			//若断言的数组为空或者没有元素，抛出throwIfAssertFail异常
			throw throwIfAssertFail;
		}
	}

	/**
	 * 断言数组没有空的元素
	 * 注意：不适用于空数组，在空数组的情况下不会报错
	 * @param array 要断言的数组
	 * @param message 若断言的数组有空的元素，所抛出的异常信息
	 * @throws IllegalArgumentException 若断言的数组有空的元素，抛出IllegalArgumentException异常
	 */
	public static void noNullElements(Object[] array, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					//若断言的数组有空的元素，抛出IllegalArgumentException异常，并使用给定的信息
					throw new IllegalArgumentException(message);
				}
			}
		}
	}

	/**
	 * 断言数组没有空的元素
	 * 注意：不适用于空数组，在空数组的情况下不会报错
	 * @param array 要断言的数组
	 * @throws IllegalArgumentException 若断言的数组有空的元素，抛出IllegalArgumentException异常
	 */
	public static void noNullElements(Object[] array) {
		//调用noNullElements(array, "[Assertion failed] - this array must not contain any null elements")方法
		noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
	}

	/**
	 * 断言数组没有空的元素
	 * 注意：不适用于空数组，在空数组的情况下不会报错
	 * @param array 要断言的数组
	 * @param throwIfAssertFail 若断言的数组有空的元素，所抛出的异常
	 */
	public static void noNullElements(Object[] array, RuntimeException throwIfAssertFail) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					//若断言的数组有空的元素，抛出throwIfAssertFail异常
					throw throwIfAssertFail;
				}
			}
		}
	}

	/**
	 * 断言一个集合有元素。即集合不能为<code>null</code>并且有至少一个元素
	 * @param collection 要断言的集合
	 * @param message 若集合为null或者没有元素，抛出的异常信息
	 * @throws IllegalArgumentException 如果集合为null或者没有元素，抛出IllegalArgumentException异常
	 */
	public static <E> void notEmpty(Collection<E> collection, String message) {
		if (CollectionUtils.isEmpty(collection)) {
			//如果集合为null或者没有元素，抛出IllegalArgumentException异常，并使用给定的信息
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言一个集合有元素。即集合不能为<code>null</code>并且有至少一个元素
	 * @param collection 要断言的集合
	 * @throws IllegalArgumentException 如果集合为null或者没有元素，抛出IllegalArgumentException异常
	 */
	public static <E> void notEmpty(Collection<E> collection) {
		notEmpty(collection,
				"[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	/**
	 * 断言一个集合有元素。即集合不能为<code>null</code>并且有至少一个元素
	 * @param collection 要断言的集合
	 * @param throwIfAssertFail 如果集合为null或者没有元素，抛出throwIfAssertFail异常
	 */
	public static <E> void notEmpty(Collection<E> collection, RuntimeException throwIfAssertFail) {
		if (CollectionUtils.isEmpty(collection)) {
			//如果集合为null或者没有元素，抛出throwIfAssertFail异常
			throw throwIfAssertFail;
		}
	}

	/**
	 * 断言一个Map有键值对，即Map不能为<code>null</code>并且至少有一个键值对
	 * @param map 要检查的键值对
	 * @param message 若Map没有键值对，所抛出的异常信息
	 * @throws IllegalArgumentException 若Map没有键值对，抛出IllegalArgumentException异常
	 */
	public static <k,V> void notEmpty(Map<k,V> map, String message) {
		if (CollectionUtils.isEmpty(map)) {
			//若Map没有键值对，抛出IllegalArgumentException异常，并使用给定的信息
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言一个Map有键值对，即Map不能为<code>null</code>并且至少有一个键值对
	 * @param map 要检查的键值对
	 * @throws IllegalArgumentException 若Map没有键值对，抛出IllegalArgumentException异常
	 */
	public static <K,V> void notEmpty(Map<K,V> map) {
		//调用notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry")方法
		notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	/**
	 * 断言一个Map有键值对，即Map不能为<code>null</code>并且至少有一个键值对
	 * @param map 要检查的键值对
	 * @param throwIfAssertFail  若Map没有键值对，抛出throwIfAssertFail异常
	 */
	public static <K,V> void notEmpty(Map<K,V> map, RuntimeException throwIfAssertFail) {
		if (CollectionUtils.isEmpty(map)) {
			//若Map没有键值对，抛出throwIfAssertFail异常
			throw throwIfAssertFail;
		}
	}

	/**
	 * 断言给定的对象是否是指定类的实例
	 * @param type 指定的类
	 * @param obj 给定的对象
	 * @param message 若给定的对象不是指定类的实例，所抛出的部分异常信息，因为只是异常信息的开头，所以一般以“：”或“.”结尾看起来更自然
	 * @throws IllegalArgumentException 若给定的对象不是指定类的实例，抛出IllegalArgumentException异常
	 * @see Class#isInstance
	 */
	public static <T> void isInstanceOf(Class<T> type, Object obj, String message) {
		//调用notNull(type, "Type to check against must not be null")方法，若type为null则抛出异常
		notNull(type, CHECK_TYPE_NOT_NULL);
		if (!type.isInstance(obj)) {
			//若给定的对象不是指定类的实例，抛出IllegalArgumentException异常，并使用给定的信息加其他输出（实例类必须为给定的类）
			throw new IllegalArgumentException(message + "Object of class ["
					+ (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
		}
	}

	/**
	 * 断言给定的对象是否是指定类的实例
	 * @param clazz 指定的类（不知道为什么这里用clazz，上下用type）
	 * @param obj 给定的对象
     * @throws IllegalArgumentException 若给定的对象不是指定类的实例，抛出IllegalArgumentException异常
	 * @see Class#isInstance
	 */
	public static <T> void isInstanceOf(Class<T> clazz, Object obj) {
		//调用isInstanceOf(clazz, obj, "")方法
		isInstanceOf(clazz, obj, "");
	}

	/**
	 * 断言给定的对象是否是指定类的实例
	 * @param type 指定的类
	 * @param obj 给定的对象
	 * @param throwIfAssertFail 若给定的对象不是指定类的实例，抛出throwIfAssertFail异常
	 * @see Class#isInstance
	 */
	public static <T> void isInstanceOf(Class<T> type, Object obj, RuntimeException throwIfAssertFail) {
		//调用notNull(type, "Type to check against must not be null")方法，若type为null则抛出异常
		notNull(type, CHECK_TYPE_NOT_NULL);
		if (!type.isInstance(obj)) {
			//若给定的对象不是指定类的实例，抛出throwIfAssertFail异常
			throw throwIfAssertFail;
		}
	}

	/**
	 * 断言一个类可以继承或赋值给另一个类
	 * @param superType 继承或赋值的类
	 * @param subType 被继承或赋值的类
	 * @param message 若superType不能继承或赋值给subType，所抛出的部分异常信息
	 * @throws IllegalArgumentException 若superType不能继承或赋值给subType，抛出IllegalArgumentException异常
	 */
	public static <T> void isAssignable(Class<T> superType, Class<T> subType, String message) {
		//检测superType是否为null，若为null，抛出异常
		notNull(superType, CHECK_TYPE_NOT_NULL);
		if (subType == null || !superType.isAssignableFrom(subType)) {
			//若subType为null或者superType不能继承或赋值给subType，则抛出IllegalArgumentException异常，异常信息为message++ subType + " is not assignable to " + superType
			throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
		}
	}

	/**
	 * 断言一个类可以继承或赋值给另一个类
	 * @param superType 继承或赋值的类
	 * @param subType 被继承或赋值的类
	 * @throws IllegalArgumentException 若superType不能继承或赋值给subType，抛出IllegalArgumentException异常
	 */
	public static <T> void isAssignable(Class<T> superType, Class<T> subType) {
		//调用isAssignable(superType, subType, "")方法
		isAssignable(superType, subType, "");
	}

	/**
	 * 断言布尔语句为True，和isTrue不同的地方在于抛出的异常类型不同
	 * @param expression 断言的布尔语句
	 * @param message 若布尔语句为Fale，抛出的异常信息
	 * @throws IllegalStateException 若布尔语句为False，抛出IllegalStateException异常
	 */
	public static void state(boolean expression, String message) {
		if (!expression) {
			//若布尔语句为False，抛出IllegalStateException异常，并使用给定的信息
			throw new IllegalStateException(message);
		}
	}

	/**
	 * 断言布尔语句为True，和isTrue不同的地方在于抛出的异常类型不同
	 * @param expression 断言的布尔语句
	 * @throws IllegalStateException 若布尔语句为False，抛出IllegalStateException异常
	 */
	public static void state(boolean expression) {
		//调用state(expression, "[Assertion failed] - this state invariant must be true")方法
		state(expression, "[Assertion failed] - this state invariant must be true");
	}
}
