package net.diaowen.common.utils;

import java.util.regex.Pattern;

/**
 * 	用正则表达式检查一个字符串是否为纯数字
 */
public class NumberUtils {
	private NumberUtils(){

	}
	// 用正则表达式检查一个字符串是否为纯数字
	public static boolean isNumeric(String str){
		// 创建一个匹配纯数字的正则表达式模式
	    Pattern pattern = Pattern.compile("[0-9]*");
		// 通过模式匹配器检查给定的字符串是否完全匹配纯数字模式
	    return pattern.matcher(str).matches();    
	 } 
}
