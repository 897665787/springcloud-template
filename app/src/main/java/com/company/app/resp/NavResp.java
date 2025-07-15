package com.company.app.resp;

import com.company.framework.jackson.annotation.AutoDesc;
import com.company.tool.api.enums.NavItemEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NavResp extends com.company.tool.api.response.NavResp {
    /**
     * 跳转类型
     */
    @AutoDesc(value = NavItemEnum.Type.class)
    NavItemEnum.Type type;
}
