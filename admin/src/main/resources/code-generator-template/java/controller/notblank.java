		if (StringUtils.isNotBlank({modelName}.get{ColumnName}())) {
			wrapper.like("{column_name}", {modelName}.get{ColumnName}());
		}
