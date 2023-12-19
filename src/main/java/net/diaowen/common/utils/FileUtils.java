package net.diaowen.common.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Random;

/**
 * 文件转移、保存到指定路径、数据传送到指定路径、删除、读文件
 */
public class FileUtils {
	private FileUtils(){

	}
	private static Random r = new Random();
	/**
	 * 将上传的文件转移到指定路径
	 *
	 * @param path 目标路径
	 * @param file 上传的文件
	 * @return 转移后的文件
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static File transferFile(String path, MultipartFile file)
			throws IllegalStateException, IOException {
		//创建相应路径的File实体
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory()) {
			//如果相应文件不存在或者不是文件夹，就创建相应的文件夹
			dir.mkdirs();
		}

		if (file.getOriginalFilename().lastIndexOf(".") > 0) {
			//如果原文件名最后的“.”后面跟着的不是空字符串，就创建一个File的实例，以path+原先的文件名为路径
			File aFile = new File(path + file.getOriginalFilename());
			// 获取目标路径的上级目录路径
			String strLast = path
					.substring(0, path.lastIndexOf(File.separator));
			// 计算文件名长度，包括上级目录路径长度、文件名长度以及分隔符的长度
			int nameLength = strLast.substring(
					strLast.lastIndexOf(File.separator)).length()
					+ 1 + aFile.getName().length();
			// 如果上传的文件的名字中含有中文字符或其他非单词字符，那么就进行重命名，并将其改为英文名字
			// 这里所说的单词字符为：[a-zA-Z_0-9]
			Boolean rename = true;
			//// 检查文件是否存在或者是否已命名，或者文件名长度是否大于30个字符，如果是，则进入重命名流程
			if (aFile != null && aFile.exists() || nameLength > 30 || rename) {
				// 创建一个包含所有可能字符的数组，用于随机生成文件名
				char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
						'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
						'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
						'5', '6', '7', '8', '9' };

				// 创建一个 StringBuffer 用于存储文件名
				StringBuilder fileName = new StringBuilder("");

				// 初始化位置变量为 -1
				int pos = -1;
				// 循环15次，生成随机文件名
				for (int i = 0; i < 15; i++) {
					// 生成0到36之间的随机数，并取绝对值
					pos = Math.abs(r.nextInt(36));
					// 根据随机数，从字符数组中取字符，并添加到文件名字符串中
					fileName.append(str[pos]);
				}


				// 从原始文件名中获取文件扩展名，并赋值给 newName
				String newName = file.getOriginalFilename().substring(
						file.getOriginalFilename().lastIndexOf(".") + 1);

				// 创建新的文件，路径为原路径加上随机生成的文件名及扩展名
				aFile = new File(path + fileName.toString().trim() + "."
						+ newName);

			}
			// 将上传的文件内容转移到新文件中
			file.transferTo(aFile);
			//返回aFile实例
			return aFile;
		} else {
			//如果文件最后的“.”后面没有跟着字符串，则返回null
			return null;
		}
	}

	/**
	 * 将上传的文件保存到指定路径下
	 *
	 * @param path 保存文件的路径
	 * @param file 要保存的文件
	 * @return 保存后的文件对象
	 * @throws IllegalStateException 如果文件状态异常
	 * @throws IOException           输入输出异常
	 */
	public static File transferFile1(String path, MultipartFile file)
			throws IllegalStateException, IOException {
		//创建相应路径的File实例
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory()) {
			//如果文件不存在或者不是文件夹，就创建相应文件夹
			dir.mkdirs();
		}

		if (file.getOriginalFilename().lastIndexOf(".") > 0) {
			//如果原文件名最后的“.”后面跟着的不是空字符串，就创建一个File的实例，以path+原先的文件名为路径
			File aFile = new File(path + file.getOriginalFilename());

			// 获取路径中文件名之前的部分
			String strLast = path
					.substring(0, path.lastIndexOf(File.separator));

			//计算文件名长度
			int nameLength = strLast.substring(
					strLast.lastIndexOf(File.separator)).length()
					+ 1 + aFile.getName().length();
			// 如果上传的文件的名字中含有中文字符或其他非单词字符，那么就进行重命名，并将其改为英文名字
			// 这里所说的单词字符为：[a-zA-Z_0-9]
			Boolean rename = true;

			if (aFile != null && aFile.exists() || nameLength > 30 || rename) {
				//如果文件不等于空并且文件已经存在或者名字长度大于30，则重命名
				// 创建一个包含所有可能字符的数组，用于随机生成文件名
				char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
						'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
						'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
						'5', '6', '7', '8', '9' };

				// 创建一个 StringBuffer 用于存储文件名
				StringBuilder fileName = new StringBuilder("");

				// 初始化位置变量为-1
				int pos = -1;
				// 循环15次，生成随机文件名
				for (int i = 0; i < 15; i++) {
					// 生成0到36之间的随机数，并取绝对值
					pos = Math.abs(r.nextInt(36));
					// 根据随机数，从字符数组中取字符，并添加到文件名字符串中
					fileName.append(str[pos]);
				}

				// 从原始文件名中获取文件扩展名，并赋值给 newName
				String newName = file.getOriginalFilename().substring(
						file.getOriginalFilename().lastIndexOf(".") + 1);
				// 创建新的文件，路径为原路径加上随机生成的文件名及扩展名
				aFile = new File(path + fileName.toString().trim() + "."
						+ newName);
			}
			// 将上传的文件内容转移到新文件中
			file.transferTo(aFile);
			//返回aFile实例
			return aFile;
		} else {
			//如果文件最后的“.”后面没有跟着字符串，则返回null
			return null;
		}
	}


	/**
	 * 将文件数组传输到指定路径下
	 *
	 * @param path      保存文件的路径
	 * @param file      要保存的文件数组
	 * @param filenames 文件名数组
	 * @return 保存后的文件名数组
	 * @throws IllegalStateException 如果文件状态异常
	 * @throws IOException           输入输出异常
	 */
	public static String[] transferFile2(String path, File[] file,String[] filenames)
			throws IllegalStateException, IOException {
		// 创建指定路径的File实例
		File file2=new File(path);
		if (!file2.exists() || !file2.isDirectory()) {
			// 如果文件不存在或者不是文件夹，就创建相应文件夹
			file2.mkdirs();
		}
		if (file != null && file.length > 0) {
			// 如果文件数组不为空且长度大于0

			// 创建一个临时字符串数组

			String[] temp=new String[file.length];
			// 创建包含所有可能字符的数组，用于生成随机文件名
			char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
					'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
					'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
					'8', '9' };
			for (int i = 0; i < file.length; i++) {
				try {







					try (FileInputStream fis = new FileInputStream(file[i])) {
						//创建一个用于存储文件名的 StringBuffer
						StringBuilder fileName = new StringBuilder("");
						// 初始化位置变量为-1
						int pos = -1;
						// 生成15个随机字符组成的文件名
						for (int j = 0; j < 15; j++) {
							//随机数范围为0带36，且取绝对值
							pos = Math.abs(r.nextInt(36));
							// 将随机字符添加到文件名中
							fileName.append(str[pos]);
						}
						// 将文件后缀名添加到生成的文件名后面
						fileName.append(filenames[i].substring(filenames[i].lastIndexOf(".")));

						// 写入文件流
						try (FileOutputStream fos = new FileOutputStream(path + fileName.toString())) {
							// 创建一个缓冲区，用于存储读取的文件数据
							byte[] buf = new byte[1024];
							//初始化变量j=0
							int j = 0;

							while ((j = fis.read(buf)) != -1) {
								// 从文件输入流读取数据到缓冲区，然后写入到文件输出流中
								fos.write(buf, 0, j);
							}
							// 将生成的文件名存储在临时数组中
							temp[i]=fileName.toString();
						} catch (IOException e) {
							e.printStackTrace();
						}

						temp[i] = fileName.toString();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					// 捕获异常并打印堆栈信息
					e.printStackTrace();
				}
			}
			// 返回保存后的文件名数组
			return temp;
		}
		//如果文件为空或长度小于对于0，返回null
		return new String[0];
	}

	/**
	 * 删除文件或者文件夹，对于文件夹遍历其子文件夹进行递归删除
	 *
	 * @param f -要删除的文件或目录
	 *            File对象
	 * @return 删除是否成功
	 */
	public static boolean deleteFile(File f) {
		// 如果文件存在
		if (f.exists()) {
			if (f.isFile())
				// 如果是文件，直接删除
				return f.delete();
			else if (f.isDirectory()) {
				// 如果是目录，获取目录下的文件
				File[] files = f.listFiles();
				for (int i = 0; i < files.length; i++) {
					// 递归调用删除目录下的所有文件
					if (!deleteFile(files[i]))
						// 如果删除失败，返回false
						return false;
				}
				// 删除目录本身
				return f.delete();
			} else
				// 既不是文件也不是目录，返回false
				return false;
		} else
			// 文件或目录不存在，返回false
			return false;
	}

	/**
	 * 读文件
	 * @param path 文件路径
	 * @param encoding 文件编码方式
	 * @return 文件内容字符串
	 */
	public static String readfile(String path, String encoding) {
		// 用于存储文件内容的 StringBuffer
		StringBuilder stringBuffer=new StringBuilder();

		// 创建相应路径文件实体
		File inputPath = new File(path);
		if (inputPath.exists()) {
			// 如果文件存在
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(inputPath), encoding))) {
				//初始化字符串line
				String line;
				while ((line = reader.readLine()) != null) {
					// 读取非空行并追加到 StringBuffer 中
					if (StringUtils.isNotBlank(line)) {
						stringBuffer.append(line + "\r\n");
					}
				}
			} catch (IOException e) {
				// 捕获异常并打印堆栈信息
				e.printStackTrace();
			}
		}
		// 返回读取到的文件内容字符串
		return stringBuffer.toString();
	}
}
