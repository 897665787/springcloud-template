package com.company.tool.api.response;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommonI18nResp {
    private Integer id;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务ID
     */
    private Integer businessId;

    /**
     * 国际化语言编码
     */
    private String locale;

    /**
     * 文案
     */
    private String text;

    /**
     * 国际化文案
     */
    private String i18nText;

    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
