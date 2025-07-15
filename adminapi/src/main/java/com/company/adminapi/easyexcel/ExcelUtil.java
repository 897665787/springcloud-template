package com.company.adminapi.easyexcel;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import cn.hutool.core.net.URLEncodeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * excel工具
 */
@Slf4j
public class ExcelUtil {

	public static <T> void write2httpResponse(HttpServletResponse response, String sheetName, Class<?> headClass,
											  List<T> excelList) {
		ServletOutputStream outputStream;
		try {
			outputStream = response.getOutputStream();
		} catch (IOException e) {
			log.error("导出失败", e);
			throw new RuntimeException("导出失败");
		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("UTF-8");

		ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.XLSX;
		// 设置导出文件名称（避免乱码）
		String fileName = sheetName + "-" + System.currentTimeMillis() + excelTypeEnum.getValue();
		// 解决文件名中文乱码问题
		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
		response.setHeader("Content-disposition", "attachment;filename=" + URLEncodeUtil.encode(fileName));

		EasyExcel.write(outputStream)// 设置输出流
				.head(headClass)// 设置表头
				.excelType(excelTypeEnum)// 设置excel类型
				.sheet(sheetName)// 设置sheet名称
				.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())// 设置宽度自适应内容最大值
				.doWrite(excelList);
	}
}
