package com.company.admin.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.company.admin.exception.ExceptionConsts;
import com.company.common.exception.BusinessException;

/**
 * Excel相关工具类
 *（导入导出示例可参考：mscrm项目和otom项目）
 * Created with IntelliJ IDEA.
 * User: guobaikun
 * Date: 2017/11/27
 * Time: 10:01
 */

public class ExcelUtil {

    private final static String excel2003L = ".xls";    //2003- 版本的excel

    private final static String excel2007U = ".xlsx";   //2007+ 版本的excel

    /**
     * 根据文件后缀，自适应上传文件的版本
     */
    public static Workbook getWorkbook(MultipartFile file) throws Exception {
        Workbook wb = null;

        String fileName = file.getOriginalFilename();
        // 获取图片的扩展名
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        if (excel2003L.equals(suffix)) {
            wb = new HSSFWorkbook(file.getInputStream());  //2003-
        } else if (excel2007U.equals(suffix)) {
            wb = new XSSFWorkbook(file.getInputStream());  //2007+
        } else {
            throw new BusinessException("EXCEL的文件格式有,只支持.xls和.xlsx");
        }
        return wb;
    }

    /**
     * 检验模板标题是否正确（只能验证一级标题）
     */
    public static void validateHeader(Sheet sheet, String[] header) {
        //检验模板格式
        Row r = sheet.getRow(0);
        if (r == null) return;
        for (int i = 0; i < header.length; i++) {
            Cell cell = r.getCell(i);
            if (cell == null) {
                throw ExceptionConsts.EXCEL_TEMPLATE_FORMAT_WRONG;
            }
            String v = cell.getStringCellValue().replace("*", "");
            if (!v.trim().equals(header[i])) {
                throw ExceptionConsts.EXCEL_TEMPLATE_FORMAT_WRONG;
            }

        }
    }

    /**
     * 设置表头的单元格样式
     */
    public static CellStyle getHeadStyle(XSSFWorkbook wb) {
        // 创建单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        // 设置单元格的背景颜色为淡蓝色
        //cellStyle.setFillBackgroundColor(IndexedColors.PALE_BLUE.getIndex());
        //cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 创建单元格内容显示不下时自动换行
        //cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        font.setBold(true);
        //font.setColor(IndexedColors.WHITE.getIndex());
        //font.setFontName("宋体");
        //font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        //设置单元格边框样式
        //cellStyle.setBorderTop(BorderStyle.THIN);
        //cellStyle.setBorderLeft(BorderStyle.THIN);
        //cellStyle.setBorderBottom(BorderStyle.THIN);
        //cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }

    /**
     * 设置表体的单元格样式
     */
    public static XSSFCellStyle getBodyStyle(XSSFWorkbook wb) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 创建单元格内容显示不下时自动换行
        //cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        //设置单元格边框样式
        //cellStyle.setBorderTop(BorderStyle.THIN);
        //cellStyle.setBorderLeft(BorderStyle.THIN);
        //cellStyle.setBorderBottom(BorderStyle.THIN);
        //cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }

    /**
     * 自动调整列宽
     */
    public static void autoSizeAllColumn(Sheet sheet){
        List<Integer> rows = new ArrayList<>();
        for(int i=0;i<=sheet.getLastRowNum();i++){
            rows.add(sheet.getRow(i).getPhysicalNumberOfCells());
        }
        Integer maxCol = Collections.max(rows);
        for(int i=0; i<maxCol; i++){
            sheet.autoSizeColumn((short)i);
            //setSizeColumn(sheet, i);
        }
    }

    // 自适应宽度(中文支持)
    private static void setSizeColumn(Sheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                XSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = (XSSFRow) sheet.createRow(rowNum);
                } else {
                    currentRow = (XSSFRow) sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    XSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }


    /**
     * 获取单元格各类型值，返回字符串类型
     */
    public static String getCellValue(Cell cell) {
        String value = "";
        // 以下是判断数据的类型
        switch (cell.getCellTypeEnum()) {

            case NUMERIC:
                value = cell.getNumericCellValue() + "";
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } else {
                        value = "";
                    }
                } else {
                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                }
                break;
            case STRING: // 字符串
                value = cell.getStringCellValue();
                break;
            case BOOLEAN: // Boolean
                value = cell.getBooleanCellValue() + "";
                break;
            case FORMULA: // 公式
                value = cell.getCellFormula() + "";
                break;
            case BLANK: // 空值
                value = "";
                break;
            case _NONE: // 空值
                value = "";
                break;
            case ERROR: // 故障
                value = "非法字符";
                break;
            default:
                value = "未知类型";
                break;
        }
        return value;
    }

    /**
     * 生成表格
     * @param head 标题
     * @param data 内容
     * @return
     */
    public static XSSFWorkbook generateExport(List<String> head, List<List<String>> data) {
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet();
        CellStyle headStyle = getHeadStyle(workBook);
        //标题
        XSSFRow hssfRow = sheet.createRow(0);
        for (int i = 0; i < head.size(); i++){
            XSSFCell cell = hssfRow.createCell(i);
            cell.setCellValue(head.get(i));
            cell.setCellStyle(headStyle);
        }

        // 构建表体数据
        XSSFCell cell;
        if (!CollectionUtils.isEmpty(data)){
            for (int i = 0; i < data.size(); i++){
                XSSFRow bodyRow = sheet.createRow(i + 1);
                List<String> item = data.get(i);
                for(int j = 0; j < item.size(); j++){
                    cell = bodyRow.createCell(j);
                    cell.setCellValue(item.get(j));
                }
            }
        }
        //自动调整所有列宽
        autoSizeAllColumn(sheet);
        return workBook;
    }

}
