package net.diaowen.common.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 这个类提供了读取 Excel 文件的功能。
 * 可以读取不同格式的 Excel 文件（xls 和 xlsx）。
 * 它包含了读取文件、获取单元格值等方法。
 */
public class ReadExcelUtil {

	/**
	 *
	 * @param filePath Excel文件路径
	 * @return Excel工作簿对象
	 */
	public static Workbook getWorkBook(String filePath){
		POIFSFileSystem fs;
		Workbook wb = null;
		try {
			// 尝试使用XSSFWorkbook读取文件
			wb = new XSSFWorkbook(filePath);
		} catch (Exception e) {
			// 如果XSSFWorkbook抛出异常，则尝试使用HSSFWorkbook读取文件
			try {
				fs = new POIFSFileSystem(new FileInputStream(filePath));
				wb = new HSSFWorkbook(fs);
			} catch (IOException e1) {
				// 处理读取文件时的IO异常
				e1.printStackTrace();
			}
		}
		return wb;
	}


	/**
	 * 获取给定工作簿中指定索引位置的HSSFSheet对象。
	 *
	 * @param wb    HSSFWorkbook对象
	 * @param index 索引位置
	 * @return HSSFSheet对象
	 */
	public static HSSFSheet getHSSFSheet(HSSFWorkbook wb, int index) {
		HSSFSheet sheet = wb.getSheetAt(0);
		return sheet;
	}
	/**
	 * 根据给定行和列索引获取单元格的值。
	 *
	 * @param sfrow 给定的行对象
	 * @param col   列索引
	 * @return 单元格的值，如果单元格为空，则返回空字符串
	 */
	//根据行和列获取值
	public static String getValueByRowCol(Row sfrow, int col){
		//获取指定行的指定列的单元格
		Cell cell = sfrow.getCell((short)col);
		//如果单元格为空，返回空字符串
		if (cell == null)
			return "";
		//获取单元格的字符串值
		String msg = getCellStringValue(cell);
		return msg;
	}
	/**
	 * 根据给定的单元格获取其值。
	 *
	 * @param sfCell 给定的单元格对象
	 * @return 单元格的值
	 */
	//根据列获取值
	public static String getValueByCol(Cell sfCell){
		//获取单元格字符串值
		String msg = getCellStringValue(sfCell);
		return msg;
	}

	/**
	 * 从给定的文件路径读取Excel文件内容。
	 *
	 * @param filePath 文件路径
	 */
	public static void reader(String filePath) {
		try {
			// 创建POIFSFileSystem对象，用于读取文件
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
			// 创建HSSFWorkbook对象，用于读取文件
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// 获取第一个sheet
			HSSFSheet sheet = wb.getSheetAt(0);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取单元格的字符串值。
	 *
	 * @param cell 给定的单元格对象
	 * @return 单元格的字符串值
	 */
	public static String getCellStringValue(Cell cell) {
		// 定义一个字符串变量用于存储单元格的值
		String cellValue = "";
		// 根据单元格的类型，判断单元格的值
		switch (cell.getCellType()) {
			// 如果单元格是字符串类型
			case STRING:
				// 获取单元格的值
				cellValue = cell.getStringCellValue();
				// 如果单元格的值为空或者长度小于等于0，则设置单元格的值为空格
				if (cellValue.trim().equals("") || cellValue.trim().length() <= 0) {
					cellValue = " ";
				}
				break;
			// 如果单元格是数字类型
			case NUMERIC:

				// 设置格式
				DecimalFormat formatter = new DecimalFormat("######");
				// 格式化单元格的值
				cellValue = formatter.format(cell.getNumericCellValue());
				break;
			// 如果单元格是公式类型
			case FORMULA:
				// 设置单元格的类型为数字
				cell.setCellType(CellType.NUMERIC);
				// 获取单元格的值
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			// 如果单元格是空白类型
			case BLANK:
				// 设置单元格的值为空格
				cellValue = " ";
				break;
			// 如果单元格是布尔类型
			case BOOLEAN:
				break;
			// 如果单元格是错误类型
			case ERROR:
				break;
			// 如果单元格是其他类型
			default:
				break;
		}
		// 返回单元格的值
		return cellValue;
	}
	/**
	 * 获取给定工作表的行数。
	 *
	 * @param sheet 给定的工作表对象
	 * @return 给定工作表的行数
	 */
	public static int getRowSize(Sheet sheet){
		return sheet.getLastRowNum();
	}
	/**
	 * 获取给定行对象中的单元格数量。
	 *
	 * @param sfRow 给定的行对象
	 * @return 给定行中的单元格数量
	 */
	public static int getCellSize(HSSFRow sfRow){
		return sfRow.getLastCellNum();
	}
	/**
	 * 主方法，用于读取名为 "terchers.xls" 的文件。
	 * 注意：方法内部已经被注释，需要根据需要调用。
	 *
	 * @param args 参数列表（未使用）
	 */
	public static void main(String[] args) {
		reader("F://terchers.xls");
	}
}
