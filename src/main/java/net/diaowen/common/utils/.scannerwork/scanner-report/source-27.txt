package net.diaowen.common.utils.parsehtml;

import java.util.regex.Pattern;

/**
 * HTML内容解析工具类
 */
public class HtmlUtil {
	private HtmlUtil(){

	}
	public static String removeTagFromText(String htmlStr) {
		if (htmlStr == null || "".equals(htmlStr))
			return "";
		String textStr = "";
		Pattern pattern;
		java.util.regex.Matcher matcher;

		try {
			String regEx_remark = "<!--.+?-->";

			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";


			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";

			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			htmlStr = htmlStr.replace("\n", "");
			htmlStr = htmlStr.replace("\t", "");
			htmlStr = htmlStr.replace("\r", "");

			// 过滤注释标签
			pattern = Pattern.compile(regEx_remark);// 过滤注释标签
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll("");

			pattern = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤script标签

			pattern = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤style标签

			pattern = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤html标签

			pattern = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤html标签

			htmlStr = htmlStr.replaceAll("\n[\\s| ]*\r", "");
			htmlStr = htmlStr.replaceAll("<(.*)>(.*)<\\/(.*)>|<(.*)\\/>", "");

			textStr = htmlStr.trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr;// dwv402880e666e15b790166e16222000000 返回文本字符串
	}
}
