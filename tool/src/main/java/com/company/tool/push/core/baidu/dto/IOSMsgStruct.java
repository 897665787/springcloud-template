package com.company.tool.push.core.baidu.dto;

import lombok.Data;

/**
 * iOS通知格式二
 * {
 *     "aps": {
 *         "alert": {
 *             "summary-arg": "Baidu Cloud Summary", // 可选
 *             "title": "Message From Baidu Cloud Push-Service", // 可选
 *             "body": "desc" // 可选
 *         },
 *         "mutable-content": 1,
 *         "sound": "default", // 可选
 *         "badge": 0, // 可选
 *         "thread-id": "sample" // 可选
 *     },
 *     "key1": "value1",
 *     "key2": "value2"
 * }
 *
 * <pre>
 * 官方文档：https://push.baidu.com/doc/restapi/msg_struct
 * </pre>
 */
@Data
public class IOSMsgStruct {
    private Aps aps;

    @Data
    public static class Aps {
        private Alert alert;

        @Data
        public static class Alert {
            private String title;
            private String body;
        }
    }

}
