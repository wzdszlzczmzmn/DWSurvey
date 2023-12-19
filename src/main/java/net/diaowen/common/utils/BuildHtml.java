package net.diaowen.common.utils;

import java.io.*;

public class BuildHtml {

	/**
	 * 内容输入到本地
	 * @param fileName
	 * @param fileRealPath
	 * @param os
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static File writeLocal(String fileName, String fileRealPath,
									 final ByteArrayOutputStream os) throws IOException,
			FileNotFoundException {
		File file2 = new File(fileRealPath);
		if (!file2.exists() || !file2.isDirectory()) {
			file2.mkdirs();
		}
		File file = new File(fileRealPath + fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		os.writeTo(fos);
		fos.close();
		return file;
	}

}
