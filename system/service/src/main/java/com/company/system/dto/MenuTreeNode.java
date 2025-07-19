package com.company.system.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuTreeNode {

    /**
     * id
     */
    private Integer id;

    /**
     * 父菜单ID
     */
    private Integer parentId;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 菜单类型(M:目录,C:菜单,F:按钮)
     */
    private String menuType;

    /**
     * 菜单状态(ON:正常,OFF:停用)
     */
    private String status;

    /**
     * 菜单状态(1:显示,0:隐藏)
     */
    private Integer visible;

    private List<MenuTreeNode> children;

}
