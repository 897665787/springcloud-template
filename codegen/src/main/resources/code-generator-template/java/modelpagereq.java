package com.company.{module}.api.request.page;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * {module_name}
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class {ModelName}PageReq {
{column_field}
    /**
     * 当前页码
     */
    private Long current = 1L;

    /**
     * 每页显示的数量
     */
    private Long size = 10L;

}