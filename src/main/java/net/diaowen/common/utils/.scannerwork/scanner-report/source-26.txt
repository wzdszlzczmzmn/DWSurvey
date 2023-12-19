package net.diaowen.common.utils.parsehtml;

import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * HTML内容解析工具类
 */
public class HtmlUtil {
	/**
	 * 日志
	 */
	private static final Logger LOGGER = Logger.getLogger(HtmlUtil.class.getName());

	private HtmlUtil(){

	}
	public static String removeTagFromText(String htmlStr) {
		if (htmlStr == null || "".equals(htmlStr))
			return "";
		String textStr = "";
		Pattern pattern;
		java.util.regex.Matcher matcher;

		try {
			String regExRemark = "<!--.+?-->";

			String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";


			String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";

			String regExHtml = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regExHtml1 = "<[^>]+";
			htmlStr = htmlStr.replace("\n", "");
			htmlStr = htmlStr.replace("\t", "");
			htmlStr = htmlStr.replace("\r", "");

			// 过滤注释标签
			pattern = Pattern.compile(regExRemark);// 过滤注释标签
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll("");

			pattern = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤script标签

			pattern = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤style标签

			pattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤html标签

			pattern = Pattern.compile(regExHtml1, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤html标签

			htmlStr = htmlStr.replaceAll("\n[\\s| ]*\r", "");
			htmlStr = htmlStr.replaceAll("<(.*)>(.*)<\\/(.*)>|<(.*)\\/>", "");

			textStr = htmlStr.trim();
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}
		return textStr;// dwv402880e666e15b790166e16222000000 返回文本字符串
	}
}
