/**
 * Copyright (c) 2005-2011 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: CryptoUtils.java 764 2009-12-27 19:13:59Z calvinxiu $
 */
package net.diaowen.common.utils.security;

import net.diaowen.common.utils.EncodeUtils;
import net.diaowen.common.utils.ExceptionUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * 支持HMAC-SHA1消息签名 及 DES/AES对称加密的工具类.
 * 
 * 支持Hex与Base64两种编码方式.
 *
 */
public abstract class CryptoUtils {
	private CryptoUtils(){

	}

	// 加密算法
	private static final String DES = "DES";
	private static final String AES = "AES";
	private static final String HMACSHA1 = "HmacSHA1";

	// 默认HMACSHA1密钥大小
	private static final int DEFAULT_HMACSHA1_KEYSIZE = 160;//RFC2401
	// 默认AES密钥大小
	private static final int DEFAULT_AES_KEYSIZE = 128;

	//-- HMAC-SHA1 funciton --//
	/**
	 * 使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes HMAC-SHA1密钥
	 */
	public static byte[] hmacSha1(String input, byte[] keyBytes) {
		try {
			// 创建一个密钥
			SecretKey secretKey = new SecretKeySpec(keyBytes, HMACSHA1);
			// 获取一个Mac实例
			Mac mac = Mac.getInstance(HMACSHA1);
			// 初始化Mac实例
			mac.init(secretKey);
			// 计算Mac
			return mac.doFinal(input.getBytes());
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 使用HMAC-SHA1进行消息签名, 返回Hex编码的结果,长度为40字符.
	 *	
	 * @see #hmacSha1(String, byte[])
	 */
	//将hmacSha1加密后的字节数组转换成16进制字符串
	public static String hmacSha1ToHex(String input, byte[] keyBytes) {
		//调用hmacSha1方法，将input和keyBytes进行加密
		byte[] macResult = hmacSha1(input, keyBytes);
		//将加密后的字节数组转换成16进制字符串
		return EncodeUtils.encodeHex(macResult);
	}

	/**
	 * 使用HMAC-SHA1进行消息签名, 返回Base64编码的结果.
	 * 
	 * @see #hmacSha1(String, byte[])
	 */
	//使用hmacSha1算法，将输入字符串和keyBytes转换为base64编码
	public static String hmacSha1ToBase64(String input, byte[] keyBytes) {
		//使用hmacSha1算法，将输入字符串和keyBytes进行签名
		byte[] macResult = hmacSha1(input, keyBytes);
		//将签名结果进行base64编码
		return EncodeUtils.encodeBase64(macResult);
	}

	/**
	 * 使用HMAC-SHA1进行消息签名, 返回Base64编码的URL安全的结果.
	 * 
	 * @see #hmacSha1(String, byte[])
	 */
	public static String hmacSha1ToBase64UrlSafe(String input, byte[] keyBytes) {
		// 使用hmacSha1算法，输入input，keyBytes，计算macResult
		byte[] macResult = hmacSha1(input, keyBytes);
		// 使用encodeUrlSafeBase64算法，将macResult编码为base64UrlSafe
		return EncodeUtils.encodeUrlSafeBase64(macResult);
	}

	/**
	 * 校验Hex编码的HMAC-SHA1签名是否正确.
	 * 
	 * @param hexMac Hex编码的签名
	 * @param input 原始输入字符串
	 * @param keyBytes 密钥
	 */
	// 判断十六进制MAC是否有效
	public static boolean isHexMacValid(String hexMac, String input, byte[] keyBytes) {
		// 将十六进制字符串转换为字节数组
		byte[] expected = EncodeUtils.decodeHex(hexMac);
		// 使用hmacSha1算法计算输入字符串的MAC
		byte[] actual = hmacSha1(input, keyBytes);

		// 比较预期结果和实际结果是否相等
		return Arrays.equals(expected, actual);
	}

	/**
	 * 校验Base64/Base64URLSafe编码的HMAC-SHA1签名是否正确.
	 * 
	 * @param base64Mac Base64/Base64URLSafe编码的签名
	 * @param input 原始输入字符串
	 * @param keyBytes 密钥
	 */
	public static boolean isBase64MacValid(String base64Mac, String input, byte[] keyBytes) {
		// 将base64Mac转换为字节数组
		byte[] expected = EncodeUtils.decodeBase64(base64Mac);
		// 使用hmacSha1算法对input和keyBytes进行签名
		byte[] actual = hmacSha1(input, keyBytes);

		// 比较expected和actual是否相等
		return Arrays.equals(expected, actual);
	}

	/**
	 * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节).
	 * HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节).
	 */
	//生成MacSha1密钥
	public static byte[] generateMacSha1Key() {
		try {
			//获取HMACSHA1算法实例
			KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1);
			//初始化密钥长度
			keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
			//生成密钥
			SecretKey secretKey = keyGenerator.generateKey();
			//返回密钥字节数组
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			//抛出异常
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 生成HMAC-SHA1密钥, 返回Hex编码的结果,长度为40字符. 
	 * @see #generateMacSha1Key()
	 */
	public static String generateMacSha1HexKey() {
		return EncodeUtils.encodeHex(generateMacSha1Key());
	}

	//-- DES function --//
	/**
	 * 使用DES加密原始字符串, 返回Hex编码的结果.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes 符合DES要求的密钥
	 */
	public static String desEncryptToHex(String input, byte[] keyBytes) {
		//调用des函数，将input字符串转换为字节数组，并使用keyBytes作为密钥，使用Cipher.ENCRYPT_MODE模式进行加密
		byte[] encryptResult = des(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
		//将加密结果转换为16进制字符串
		return EncodeUtils.encodeHex(encryptResult);
	}

	/**
	 * 使用DES加密原始字符串, 返回Base64编码的结果.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes 符合DES要求的密钥
	 */
	public static String desEncryptToBase64(String input, byte[] keyBytes) {
		//调用des函数，将输入字符串加密，并返回Base64编码
		byte[] encryptResult = des(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
		return EncodeUtils.encodeBase64(encryptResult);
	}

	/**
	 * 使用DES解密Hex编码的加密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 * @param keyBytes 符合DES要求的密钥
	 */
	public static String desDecryptFromHex(String input, byte[] keyBytes) {
		byte[] decryptResult = des(EncodeUtils.decodeHex(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * 使用DES解密Base64编码的加密字符串, 返回原始字符串.
	 * 
	 * @param input Base64编码的加密字符串
	 * @param keyBytes 符合DES要求的密钥
	 */
	public static String desDecryptFromBase64(String input, byte[] keyBytes) {
		byte[] decryptResult = des(EncodeUtils.decodeBase64(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * 使用DES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param inputBytes 原始字节数组
	 * @param keyBytes 符合DES要求的密钥
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 */
	private static byte[] des(byte[] inputBytes, byte[] keyBytes, int mode) {
		try {
			// 创建一个DESKeySpec对象，用于指定密钥
			DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
			// 创建一个密钥工厂，用于生成密钥
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			// 生成密钥
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			// 创建一个Cipher对象，用于加密/解密
			Cipher cipher = Cipher.getInstance(DES);
			// 初始化Cipher对象
			cipher.init(mode, secretKey);
			// 加密/解密
			return cipher.doFinal(inputBytes);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 生成符合DES要求的密钥, 长度为64位(8字节).
	 */
	public static byte[] generateDesKey() {
		try {
			// 获取DES加密算法的密钥生成器
			KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
			// 生成密钥
			SecretKey secretKey = keyGenerator.generateKey();
			// 获取密钥的编码
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 生成符合DES要求的Hex编码密钥, 长度为16字符.
	 */
	public static String generateDesHexKey() {
		return EncodeUtils.encodeHex(generateDesKey());
	}

	//-- AES funciton --//
	/**
	 * 使用AES加密原始字符串, 返回Hex编码的结果.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes 符合AES要求的密钥
	 */
	public static String aesEncryptToHex(String input, byte[] keyBytes) {
		byte[] encryptResult = aes(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
		return EncodeUtils.encodeHex(encryptResult);
	}

	/**
	 * 使用AES加密原始字符串, 返回Base64编码的结果.
	 * 
	 * @param input 原始输入字符串
	 * @param keyBytes 符合AES要求的密钥
	 */
	public static String aesEncryptToBase64(String input, byte[] keyBytes) {
		byte[] encryptResult = aes(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
		return EncodeUtils.encodeBase64(encryptResult);
	}

	/**
	 * 使用AES解密Hex编码的加密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 * @param keyBytes 符合AES要求的密钥
	 */
	public static String aesDecryptFromHex(String input, byte[] keyBytes) {
		byte[] decryptResult = aes(EncodeUtils.decodeHex(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * 使用AES解密Base64编码的加密字符串, 返回原始字符串.
	 * 
	 * @param input Base64编码的加密字符串
	 * @param keyBytes 符合AES要求的密钥
	 */
	public static String aesDecryptFromBase64(String input, byte[] keyBytes) {
		byte[] decryptResult = aes(EncodeUtils.decodeBase64(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param inputBytes 原始字节数组
	 * @param keyBytes 符合AES要求的密钥
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 */
	private static byte[] aes(byte[] inputBytes, byte[] keyBytes, int mode) {
		try {
			SecretKey secretKey = new SecretKeySpec(keyBytes, AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(inputBytes);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 生成AES密钥,返回字节数组,长度为128位(16字节).
	 */
	public static byte[] generateAesKey() {
		try {
			// 获取AES加密算法的密钥生成器
			KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			// 初始化密钥生成器，长度为128位
			keyGenerator.init(DEFAULT_AES_KEYSIZE);
			// 生成密钥
			SecretKey secretKey = keyGenerator.generateKey();
			// 获取密钥的编码
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			// 抛出异常
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 生成AES密钥, 返回Hex编码的结果,长度为32字符. 
	 * @see #generateMacSha1Key()
	 */
	public static String generateAesHexKey() {
		return EncodeUtils.encodeHex(generateAesKey());
	}
}