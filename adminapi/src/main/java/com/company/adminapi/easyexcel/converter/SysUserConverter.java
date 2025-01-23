package com.company.adminapi.easyexcel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.NumberUtils;
import com.company.adminapi.cache.SysUserCache;
import com.company.framework.context.SpringContextUtil;
import com.company.system.api.response.SysUserResp;

/**
 * 系统用户（id转nickname）
 */
public class SysUserConverter implements Converter<Integer> {

	@Override
	public Class<?> supportJavaTypeKey() {
		return Integer.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.NUMBER;
	}

	@Override
	public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		return cellData.getNumberValue().intValue();
	}

	@Override
	public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
		SysUserCache sysUserCache = SpringContextUtil.getBean(SysUserCache.class);
		SysUserResp sysUserResp = sysUserCache.getById(context.getValue());
		if (sysUserResp == null || sysUserResp.getNickname() == null) {
			return NumberUtils.formatToCellData(context.getValue(), context.getContentProperty());
		}
		return new WriteCellData<>(sysUserResp.getNickname());
	}

}
