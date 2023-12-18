package net.diaowen.common.utils;

import net.diaowen.dwsurvey.config.DWSurveyConfig;

import java.io.*;

/**
 * 将输出流内容写到文件
 */
public class DwWriteFile {
	private DwWriteFile(){

	}

	/**
	 * OS写到文件
	 * 将输出流内容写到文件
	 * @param fileName 文件名
	 * @param fileRealPath 文件实际路径
	 * @param os 输出流
	 * @return 写入的文件
	 * @throws IOException 文件操作异常
	 * @throws FileNotFoundException 文件未找到异常
	 */
	public static File writeOS(String fileName, String fileRealPath, final ByteArrayOutputStream os) throws IOException,
			FileNotFoundException {
		//从fileRealPath字符串中找到"/wjHtml"最后一次出现的位置，并返回其后面的子字符串（不包括"/wjHtml"）
		fileRealPath = fileRealPath.substring(fileRealPath.lastIndexOf("/wjHtml")+1);
		//根路径用写好的配置
		fileRealPath = DWSurveyConfig.DWSURVEY_WEB_FILE_PATH+fileRealPath;
		//创建一个以fileRealPath为路径的File实体file
		File file = new File(fileRealPath);
		if (!file.isDirectory() || !file.exists()) {
			//如果该文件不是目录或者不存在，就创建相应的文件夹
			file.mkdirs();
		}
		//新文件路径
		File newFile = new File(fileRealPath + fileName);
		if (!newFile.exists()) {
			//如果文件不存在，就创建相应的文件
			newFile.createNewFile();
		}
		//文件输出流
		FileOutputStream fos = new FileOutputStream(newFile);
		//将输出流内容写入文件
		os.writeTo(fos);
		//关闭输出流
		fos.close();
		//返回newFile实例
		return newFile;
	}

}
