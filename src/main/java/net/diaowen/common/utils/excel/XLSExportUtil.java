package net.diaowen.common.utils.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * 这个类提供了生成导出 Excel 文件对象的功能。
 * 可以创建 Excel 文件并填充数据。
 */
public class XLSExportUtil {
	// 设置cell编码解决中文高位字节截断
	// 定制日期格式
	private static String DATE_FORMAT = " m/d/yy "; // "m/d/yy h:mm"
	// 定制浮点数格式
	private static String NUMBER_FORMAT = " #,##0.00 ";
	// 定义文件名
	private String xlsFileName;
	// 定义文件路径
	private String path;
	// 定义HSSFWorkbook对象
	private HSSFWorkbook workbook;
	// 定义HSSFSheet对象
	private HSSFSheet sheet;
	// 定义HSSFRow对象
	private HSSFRow row;
	/**
	 * 初始化Excel
	 *
	 * @param fileName
	 *            导出文件名
	 */
	// 创建XLSExportUtil对象，传入文件名和路径
	public XLSExportUtil(String fileName,String path) {
		// 设置文件名
		this.xlsFileName = fileName;
		// 设置路径
		this.path=path;
		// 创建HSSFWorkbook对象
		this.workbook = new HSSFWorkbook();
		// 创建sheet
		this.sheet = workbook.createSheet();
	}

	/** */
	/**
	 * 导出Excel文件
	 *
	 */
	public void exportXLS() throws Exception {
		try {
			// 创建文件对象
			File file=new File(path);
			// 如果文件不存在，则创建文件夹
			if(!file.exists()) {
				file.mkdirs();
			}
			// 创建文件输出流
			FileOutputStream fOut = new FileOutputStream(path+File.separator+xlsFileName);
			// 将Excel文件写入输出流
			workbook.write(fOut);
			// 将缓冲区中的数据强制输出
			fOut.flush();
			// 关闭输出流
			fOut.close();
		} catch (FileNotFoundException e) {
			// 抛出异常
			throw new Exception(" 生成导出Excel文件出错! ", e);
		} catch (IOException e) {
			// 抛出异常
			throw new Exception(" 写入Excel文件出错! ", e);
		}

	}

	/** */
	/**
	 * 增加一行
	 *
	 * @param index
	 *            行号
	 */
	public void createRow(int index) {
		this.row = this.sheet.createRow(index);
	}

	/** */
	/**
	 * 设置单元格
	 *
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	@SuppressWarnings("deprecation")
	public void setCell(int index, String value) {
		// 创建一个单元格
		HSSFCell cell = this.row.createCell((short) index);
		// 设置单元格类型为字符串
		cell.setCellType(CellType.STRING);
		// 设置单元格值为传入的value
		cell.setCellValue(value);
	}

	/** */
	/**
	 * 设置单元格
	 *
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	@SuppressWarnings("deprecation")
	// 为指定行和列设置单元格值
	public void setCell(int index, Calendar value) {
		HSSFCell cell = this.row.createCell((short) index);
		cell.setCellValue(value.getTime());
		HSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT)); // 设置cell样式为定制的日期格式
		cell.setCellStyle(cellStyle); // 设置该cell日期的显示格式
	}

	/** */
	/**
	 * 设置单元格
	 *
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	//设置单元格
	public void setCell(int index, int value) {
		//根据索引创建单元格
		HSSFCell cell = this.row.createCell((short) index);
		//设置单元格类型
		cell.setCellType(CellType.NUMERIC);
		//设置单元格值
		cell.setCellValue(value);
	}

	/** */
	/**
	 * 设置单元格
	 *
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, double value) {
		HSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(CellType.NUMERIC);
		cell.setCellValue(value);
		HSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		HSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
		cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
	}

}
