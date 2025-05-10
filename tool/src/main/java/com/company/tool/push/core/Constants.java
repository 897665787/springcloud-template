package com.company.tool.push.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface Constants {
    interface Channel {
        // 测试渠道
        String LOG = "log";
        // 友盟推送
        String YOUMENG = "youmeng";
        // 极光推送
        String JIGUANG = "jiguang";
        // 个推推送
        String GETUI = "getui";
        // 阿里云推送
        String ALI = "ali";
        // 百度云推送
        String BAIDU = "baidu";
    }

    @AllArgsConstructor
    enum DeviceType {
        Android("Android", "安卓"),
        iOS("iOS", "苹果"),
        HMOS("HMOS", "鸿蒙"),
        QuickApp("QuickApp", "快应用"),
        ;

        @Getter
        private String code;
        @Getter
        private String desc;

        public static DeviceType of(String code) {
            for (DeviceType item : DeviceType.values()) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
            return null;
        }
    }

    @AllArgsConstructor
    enum MessageType {
        NOTICE("notice", "通知"),
        TRANSPORT("transport", "透传"),
        ;

        @Getter
        private String code;
        @Getter
        private String desc;

        public static MessageType of(String code) {
            for (MessageType item : MessageType.values()) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
            return null;
        }
    }
}