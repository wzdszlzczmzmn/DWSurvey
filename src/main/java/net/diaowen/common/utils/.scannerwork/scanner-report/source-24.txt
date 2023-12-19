package net.diaowen.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * RandomUtils 类提供了各种生成随机数据和代码的方法。
 * 它包括生成随机字符串、数字、日期、订单编码、验证代码、企业编码、应用密钥和应用密钥密钥等功能。
 */
public class RandomUtils {
	private static Random random = new Random();
	public static void main(String[] args) throws Exception{
		//空的main方法
	}

	/**
	 * 生成一个规定字符串字数区间内的随机字符串
	 * @param min 字符串字数下界
	 * @param max 字符串字数上界
	 * @return
	 */
	public static String randomWord(int min,int max) {
		//初始化字符串randomStr为空字符串
		String randomStr = "";
		// 在规定字符串字数区间内生成一个随机数
		int ranNum = randomInt(min, max);
		for (int i = 0; i < ranNum; i++) {
			//初始化字符c为a
			char c = 'a';
			//将c变为26个小写英文字母中的随机1个
			c = (char) (c + (int) (Math.random() * 26));
			//将随机字符c加入到randomStr中
			randomStr += c;
		}
		//返回最终的随机生成字符串
		return randomStr;
	}

	/**
	 * 生成一个指定长度内，由随机字符串和随机数字组成的字符串
	 * @param max 目标字符串的最大长度
	 * @return 生成的字符串，包含随机字符串和随机数字
	 */
	public static String randomWordNum(int max) {
		// 生成一个介于 1 到 max 之间的随机数
		int ranNum = randomInt(1, max);
		// 计算生成随机字符串的长度
		int ranWord=max-ranNum;
		// 生成指定长度的随机字符串
		String randomWord=randomWord(ranWord, ranWord);
		if(max>3){// 若目标字符串长度大于 3
			//生成一个随机数，并转换为字符串
			String randomNum=random(ranNum-2, ranNum)+"";
			// 返回由随机数字和随机字符串组合而成的字符串
			return randomNum+randomWord;
		}
		// 返回随机字符串
		return randomWord;
	}

	/**
	 * 生成一个指定范围内的随机整数
	 *
	 * @param minNum 最小值（包含）
	 * @param maxNum 最大值（不包含）
	 * @return 在指定范围内的随机整数
	 */
	public static int randomInt(int minNum, int maxNum) {

		// 生成一个不超过 maxNum 的随机整数
		int randomInt = random.nextInt(maxNum);
		// 如果生成的随机数小于最小值
		if (randomInt < minNum) {
			// 返回最小值
			return minNum;
		}
		// 返回生成的随机数
		return randomInt;
	}

	/**
	 * 生成一个指定范围内的随机日期
	 *
	 * @param beginDate 开始日期字符串，格式为 "yyyy-MM-dd"
	 * @param endDate   结束日期字符串，格式为 "yyyy-MM-dd"
	 * @return 在指定日期范围内的随机日期
	 */
	  public static Date randomDate(String beginDate, String endDate) {
	       try {
			   // 创建日期格式化对象
	           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			   // 解析开始日期字符串
	           Date start = format.parse(beginDate);// 开始日期
			   // 解析结束日期字符串
	           Date end = format.parse(endDate);// 结束日期
	           if (start.getTime() >= end.getTime()) {// 如果开始日期晚于或等于结束日期
				   // 返回空值
	               return null;
	           }
			   // 生成在两个日期之间的随机时间戳
	           long date = random(start.getTime(), end.getTime());
			   // 返回对应的日期对象
	           return new Date(date);
	       } catch (Exception e) {
			   // 打印异常信息
	           e.printStackTrace();
	       }
		  // 返回空值
	       return null;
	   }

	/**
	 * 生成一个指定范围内的随机长整型数
	 *
	 * @param begin 范围起始值（包含）
	 * @param end   范围结束值（不包含）
	 * @return 在指定范围内的随机长整型数
	 */
	   public static long random(long begin, long end) {
			if((begin+2)>=end){// 如果起始值 + 2 大于或等于结束值
				// 修正起始值为结束值 - 2
				begin = end-2;
			}
		   // 生成一个在指定范围内的随机数
		   long rtnn = begin + (long) (Math.random() * (end - begin));
		   if (rtnn == begin || rtnn == end) {// 如果生成的随机数等于起始值或结束值
			   // 重新生成随机数（递归调用本方法）
			   return random(begin, end);
		   }
		   // 返回生成的随机数
		   return rtnn;
	   }

	/**
	 * 生成一个指定长度范围内的随机字符串
	 *
	 * @param minLen 字符串长度的下限（包含）
	 * @param maxLen 字符串长度的上限（不包含）
	 * @return 生成的随机字符串
	 */
	  public static String randomStr(int minLen,int maxLen){
		  // 可用于生成随机字符串的字符集合
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		//创建随机数生成器
		  // 用于构建生成的随机字符串
		StringBuilder sb = new StringBuilder();
		  // 生成一个介于minLen和maxLen之间的随机长度
		int length=random.nextInt(maxLen-minLen)+minLen;
		  // 根据生成的长度，随机选择字符集合中的字符拼接成字符串
		for (int i = 0; i < length; i++) {
			// 生成一个随机数，用作字符集合的索引
		        int number = random.nextInt(base.length());
			// 将随机选取的字符添加到字符串构建器中
		        sb.append(base.charAt(number));
		 }
		  // 返回生成的随机字符串
		 return sb.toString();
	  }

	/**
	 * 构建订单编码
	 *
	 * @return 返回一个订单编码，格式为“yyMMddHsSSS-randomNumber”
	 */
	public static String buildOrderCode(){
		//创建yyMMddHsSSS日期格式
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHsSSS");
		// 格式化当前日期时间为指定格式的字符串
		String dateFormat = simpleDateFormat.format(new Date());

		return dateFormat+"-"+RandomUtils.random(100000l,999999l);
	}

	/**
	 * 获取验证代码
	 *
	 * @return 返回一个在100000到999999之间的随机数作为验证代码
	 */
	public static long getVerifyCode() {
		//返回一个在100000到999999之间的随机数作为验证代码
		return random(100000l,999999l);
	}

	/**
	 * 构建特定格式的代码
	 *
	 * @return 返回一个以"yyMMddHHmmsSSS"格式的日期加上一个验证代码构成的字符串
	 */
	public static String buildCodeyyMMdd(){
		// 创建一个日期格式化对象，设置日期格式为"yyMMddHHmmsSSS"
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmsSSS");
		// 获取当前日期并以设定的格式进行格式化
		String dateFormat = simpleDateFormat.format(new Date());
		// 返回格式化后的日期字符串附加一个验证代码
		return dateFormat+"-"+RandomUtils.getVerifyCode();
//		20150806125346
	}

	/**
	 * 生成一个基于当前时间的特定格式的编码字符串
	 * 使用"yyMMddHHmmssSSS"格式获取当前日期时间并转换为字符串
	 * 结合验证代码，返回一个编码字符串
	 * 返回格式示例：yyMMddHHmmssSSS-验证代码
	 */
	public static String buildCodeyyMMdd32(){
		// 创建一个日期格式化对象，设置日期格式为"yyMMddHHmmssSSS"
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
		// 获取当前日期时间并以设定的格式进行格式化
		String dateFormat = simpleDateFormat.format(new Date());

		// 返回格式化后的日期字符串附加一个验证代码
		return dateFormat+"-"+RandomUtils.getVerifyCode();
		// 返回格式类似于 yyMMddHHmmssSSS-验证代码 的字符串
//		yyMMdd HHmmss SSS 15
	}


	/**
	 * 生成一个订单号字符串
	 * 使用"yyMMddHHmmsSSS"格式获取当前日期时间并转换为字符串
	 * 结合验证代码，返回一个订单号字符串
	 * 返回格式示例：yyMMddHHmmsSSS+验证代码
	 */
	public static String buildOrderNo(){
		// 创建一个日期格式化对象，设置日期格式为"yyMMddHHmmsSSS"
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmsSSS");
		// 获取当前日期时间并以设定的格式进行格式化
		String dateFormat = simpleDateFormat.format(new Date());

		// 返回格式化后的日期字符串附加一个验证代码
		return dateFormat+RandomUtils.getVerifyCode();
		// 返回格式类似于 yyMMddHHmmsSSS+验证代码 的订单号字符串
//		20150806125346
	}

	/**
	 * 生成企业编码
	 * 生成一个由4到6个字符组成的随机字符串
	 * 返回生成的随机字符串作为企业编码
	 */
	public static String buildEntCode() {
		//返回生成的随机字符串作为企业编码
		return randomWord(4,6);
	}

	/**
	 * 生成应用密钥
	 * 生成一个由8到10个字符组成的随机字符串，并结合另一个由8到10个字符组成的随机字符串
	 * 返回生成的随机字符串作为应用密钥
	 */
	public static String buildAppKey() {
		//返回生成的随机字符串作为应用密钥
		return randomWord(8,10)+randomStr(8,10);
	}

	/**
	 * 生成应用密钥的密钥部分
	 * 使用当前时间生成一个由数字和字符组成的字符串，并结合一个由8到10个字符组成的随机字符串
	 * 返回生成的字符串作为应用密钥的密钥部分
	 */
	public static String buildAppSecret() {

		// 返回当前时间的毫秒数结合一个由8到10个字符组成的随机字符串作为应用密钥的密钥部分
		return randomWord(8,10)+System.currentTimeMillis();
	}

}
