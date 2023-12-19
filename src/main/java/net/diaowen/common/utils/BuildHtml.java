package net.diaowen.common.utils;

import java.io.*;

/**
 * 将内存输出流的数据传到本地文件中。若没有文件相应路径，则创建路径；若没有文件，则创建文件
 */
public class BuildHtml {
	private BuildHtml(){

	}

	/**
	 * 内容输入到本地
	 * @param fileName 文件名字
	 * @param fileRealPath 文件路径
	 * @param os 内存输出流
	 * @throws IOException 输入输出异常类
	 * @throws FileNotFoundException 文件路径错误或指定文件不存在
	 */
	public static File writeLocal(String fileName, String fileRealPath,
									 final ByteArrayOutputStream os) throws IOException,
			FileNotFoundException {
		//新建file2文件，并用传入的文件路径初始化，用来检测路径是否存在
		File file2 = new File(fileRealPath);
		if (!file2.exists() || !file2.isDirectory()) {
			//如果这个路径不存在或者不是一个文件夹，那么就新建一个此路径的文件夹
			file2.mkdirs();
		}
		//新建file文件，用来表示最终写入的文件
		File file = new File(fileRealPath + fileName);
		if (!file.exists()) {
			// 如果文件不存在，尝试创建文件
			boolean isFileCreated = file.createNewFile();

			// 根据返回值处理创建文件的结果
			if (!isFileCreated) {
				// 文件创建失败的逻辑
				// 这里可以记录错误信息或执行其他操作
			}
		}
		//创建写入数据到文件的输出流fos
		FileOutputStream fos = new FileOutputStream(file);
		//讲内存中的输出流os中的内容写入到fos中
		os.writeTo(fos);
		//文件输出流关闭，确保所有的数据都被刷新到文件中，并释放资源
		fos.close();
		//返回写好的file文件
		return file;
	}

}
