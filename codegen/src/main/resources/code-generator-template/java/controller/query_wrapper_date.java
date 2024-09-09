        if (StringUtils.isNotBlank({columnName}Start)) {
            queryWrapper.ge("{column_name}", {columnName}Start + " 00:00:00");
        }
        if (StringUtils.isNotBlank({columnName}End)) {
            queryWrapper.le("{column_name}", {columnName}End + " 23:59:59");
        }
