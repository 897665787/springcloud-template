package com.company.common.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class SelectResp<T> {
    private T code;// 编码
    private String name;// 名称
}
