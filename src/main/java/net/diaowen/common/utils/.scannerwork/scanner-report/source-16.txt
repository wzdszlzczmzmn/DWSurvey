package net.diaowen.common.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.Calendar;

/**
 * 这个类提供了生成导出 Excel 文件对象的功能。
 * 可以创建 Excel 文件并填充数据，支持设置不同样式的单元格。
 */
public class XLSXExportUtil {

	// 设置cell编码解决中文高位字节截断
	// 定制日期格式
	private static String DATE_FORMAT = " m/d/yy "; // "m/d/yy h:mm"
	// 定制浮点数格式
	private static String NUMBER_FORMAT = " #,##0.00 ";
	//定义文件名
	private String xlsFileName;
	//定义文件路径
	private String path;
	//定义工作簿
	private XSSFWorkbook workbook;
	//定义sheet
	private XSSFSheet sheet;
	//定义行
	private XSSFRow row;
	//定义单元格样式
	private XSSFCellStyle cellStyle;
	//定义数据格式
	private XSSFDataFormat dataFormat ;
	/**
	 * 初始化Excel
	 *
	 * @param fileName
	 *            导出文件名
	 */
	// 创建XLSXExportUtil对象，传入文件名和路径
	public XLSXExportUtil(String fileName, String path) {
		// 设置文件名
		this.xlsFileName = fileName;
		// 设置路径
		this.path=path;
		// 创建XSSFWorkbook对象
		this.workbook = new XSSFWorkbook();
		// 创建sheet
		this.sheet = workbook.createSheet();
		// 创建cell样式
		this.cellStyle = workbook.createCellStyle();
		// 创建数据格式
		this.dataFormat = workbook.createDataFormat();
	}


	/** */
	/**
	 * 导出Excel文件
	 *
	 */
	public void exportXLS() throws Exception {
		try {
			// 判断文件夹是否存在，不存在则创建
			File file=new File(path);
			if(!file.exists()) {
				file.mkdirs();
			}
			// 创建文件输出流
			FileOutputStream fOut = new FileOutputStream(path+File.separator+xlsFileName);
			// 将Excel文件写入到输出流中
			workbook.write(fOut);
			// 将缓冲区中的数据强制输出到文件中
			fOut.flush();
			// 关闭文件输出流
			fOut.close();
		} catch (FileNotFoundException e) {
			throw new ExportException(" 生成导出Excel文件出错! ", e);
		} catch (IOException e) {
			throw new ExportException(" 写入Excel文件出错! ", e);
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


	public void setCellBlue(int index, String value) {
		// 创建一个单元格
		XSSFCell cell = this.row.createCell((short) index);
		// 设置单元格类型
		cell.setCellType(CellType.STRING);

		// 设置单元格样式
		cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIME.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 设置单元格值
		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}

	public void setCellYellow(int index, String value) {
		// 创建一个单元格
		XSSFCell cell = this.row.createCell((short) index);
		// 设置单元格类型
		cell.setCellType(CellType.STRING);


		// 设置单元格背景色
		cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
		// 设置单元格填充模式
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 设置单元格样式
		cell.setCellStyle(cellStyle);
		// 设置单元格值
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
	public void setCell(int index, String value) {
		//根据索引创建单元格
		XSSFCell cell = this.row.createCell((short) index);
		//设置单元格类型
		cell.setCellType(CellType.STRING);
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
	@SuppressWarnings("deprecation")
	public void setCell(int index, Calendar value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellValue(value.getTime());
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
	public void setCell(int index, int value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(CellType.NUMERIC);
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
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(CellType.NUMERIC);
		cell.setCellValue(value);
		cellStyle.setDataFormat(dataFormat.getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
		cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
	}

}
