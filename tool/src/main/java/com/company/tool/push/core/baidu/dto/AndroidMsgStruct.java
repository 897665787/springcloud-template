package com.company.tool.push.core.baidu.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * Android普通通知格式
 * {
 *     "title" : "hello" ,
 *     "description": "hello world" //必选
 *     "notification_builder_id": 0, //可选
 *     "notification_basic_style": 7, //可选
 *     "open_type":0, //可选
 *     "url": "http://developer.baidu.com", //可选
 *     "pkg_content":"", //可选
 *     "custom_content":{"key":"value"}, //可选
 *     "target_channel_id":"" //可选
 * }
 *
 * <pre>
 * 官方文档：https://push.baidu.com/doc/restapi/msg_struct
 * </pre>
 */
@Data
public class AndroidMsgStruct {
    private String title;

    private String description;

    @JSONField(name = "open_type")
    private Integer openType = 2;

    @JSONField(name = "pkg_content")
    private String pkgContent;
}
