package com.company.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface SysMenuEnum {

    @Getter
    @AllArgsConstructor
    enum Type {
        DIR("M", "目录"),
        MENU("C", "菜单"),
        BUTTON("F", "按钮"),
        ;
        private final String code;
        private final String desc;
    }

    @Getter
    @AllArgsConstructor
    enum Component {
        LAYOUT("Layout", "Layout"),
        ;
        private final String code;
        private final String desc;
    }

    @Getter
    @AllArgsConstructor
    enum Visible {
        SHOW(1, "显示"),
        HIDE(0, "隐藏"),
        ;
        private final Integer code;
        private final String desc;
    }

}
