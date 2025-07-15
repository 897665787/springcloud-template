package com.company.system.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SelectResp<T> {
    private T code;// 编码
    private String name;// 名称
}
