/**
 * Copyright (c) 2005-2011 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: EncodeUtils.java 1595 2011-05-11 16:41:16Z calvinxiu $
 */
package net.diaowen.common.utils;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 封装各种格式的编码解码工具类.
 * 
 * 1.Commons-Codec的hex/base64 编码
 * 2.Commons-Lang的xml/html escape
 * 3.JDK提供的URLEncoder
 *
 */
public abstract class EncodeUtils {
	private EncodeUtils(){

	}

	// 字母数字字符串
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	//默认的URL编码格式
	private static final String DEFAULT_URL_ENCODING = "UTF-8";

	/**
	 * Hex编码, byte[]->String.
	 */
	public static String encodeHex(byte[] input) {
		// 将 byte 数组转换为十六进制字符串
		return Hex.encodeHexString(input);
	}

	/**
	 * Hex解码, String->byte[].
	 */
	public static byte[] decodeHex(String input) {
		try {
			// 将十六进制字符串解码为 byte 数组
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			// 抛出异常，表示解码异常
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Base64编码, byte[]->String.
	 */
	public static String encodeBase64(byte[] input) {
		// 将 byte 数组进行 Base64 编码并转换为字符串
		return Base64.encodeBase64String(input);
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 */
	public static String encodeUrlSafeBase64(byte[] input) {
		// 将 byte 数组进行 URL 安全的 Base64 编码并转换为字符串
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Base64解码, String->byte[].
	 */
	public static byte[] decodeBase64(String input) {
		// 将 Base64 字符串解码为 byte 数组
		return Base64.decodeBase64(input);
	}

	/**
	 * Base62(0_9A_Za_z)编码数字, long->String.
	 */
	public static String encodeBase62(long num) {
		// 使用字母数字字符串对数字进行编码
		return alphabetEncode(num, 62);
	}

	/**
	 * Base62(0_9A_Za_z)解码数字, String->long.
	 */
	public static long decodeBase62(String str) {
		// 解码字母数字字符串为数字
		return alphabetDecode(str, 62);
	}

	/**
	 *  辅助方法，将基于字母表的字符串解码为数字
	 * @param num 要编码的数字
	 * @param base 编码的字母表的基数
	 * @return 编码后的字符串结果
	 */
	private static String alphabetEncode(long num, int base) {
		// 确保输入的数字为正数
		num = Math.abs(num);
		// 创建一个字符串构建器用于构建编码结果
		StringBuilder sb = new StringBuilder();
		// 根据指定的基数，将数字转换为对应字母表的字符串
		for (; num > 0; num /= base) {
			// 将每次取余数对应的字母添加到字符串构建器中
			sb.append(ALPHABET.charAt((int) (num % base)));
		}

		// 返回根据字母表编码后的字符串
		return sb.toString();
	}

	/**
	 *根据指定的字母表和基数，将字符串解码为对应的数字。
	 * @param str  要解码的字符串
	 * @param base 解码的字母表的基数
	 * @return 解码后的数字结果
	 */
	private static long alphabetDecode(String str, int base) {
		// 检查输入字符串是否为空，若为空则抛出异常
		AssertUtils.hasText(str);
		// 用于存储解码后的数字结果
		long result = 0;

		for (int i = 0; i < str.length(); i++) {
			// 对字符串中每个字符进行解码操作并累加到结果中
			result += ALPHABET.indexOf(str.charAt(i)) * Math.pow(base, i);
		}
		// 返回根据字母表解码后的数字结果
		return result;
	}

	/**
	 * URL 编码, Encode默认为UTF-8. 
	 */
	public static String urlEncode(String part) {
		try {
			// 对字符串进行 URL 编码
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// 抛出异常，表示编码异常
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 
	 */
	public static String urlDecode(String part) {

		try {
			// 对字符串进行 URL 解码
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// 抛出异常，表示解码异常
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Html 转码.
	 */
	public static String htmlEscape(String html) {
		// 将字符串中的 HTML 字符进行转义
		return StringEscapeUtils.escapeHtml(html);
	}

	/**
	 * Html 解码.
	 */
	public static String htmlUnescape(String htmlEscaped) {
		// 反转义 HTML 字符
		return StringEscapeUtils.unescapeHtml(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String xmlEscape(String xml) {
		// 将字符串中的 XML 字符进行转义
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String xmlUnescape(String xmlEscaped) {
		// 反转义 XML 字符
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}
}
