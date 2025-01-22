package com.company.admin.tree;

import java.util.List;

/**
 * 支持树状结构
 * Created by JQ棣 on 2017/10/24.
 */
public interface XSTreeable<T, V extends XSTreeable<T, V>> {

    /**
     * 获取树节点id
     * @return id
     */
    T getNodeId();

    /**
     * 获取父节点id
     * @return 父id
     */
    T getParentNodeId();

    /**
     * 增加子节点
     * @param child 子节点
     */
    void addChild(V child);

    /**
     * 获取子节点列表
     * @return 子节点列表
     */
    List<V> getChildren();
}
