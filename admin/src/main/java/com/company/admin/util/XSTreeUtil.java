package com.company.admin.util;

import java.util.*;

import com.company.admin.tree.XSTreeable;

/**
 * 树操作工具
 * Created by JQ棣 on 2017/10/25.
 */
public class XSTreeUtil {

    /**
     * 构建树状结构
     * @param list 实现XSTreeable接口的对象列表
     * @param <T> 树节点id类型
     * @param <V> 树节点类型
     * @return 树节点哈希表
     */
    public static <T, V extends XSTreeable<T, V>> Map<T, V> buildTree(List<V> list) {
        Map<T, V> map = new HashMap<>();
        for (V item : list) {
            if (item.getNodeId() != null) {
                map.put(item.getNodeId(), item);
            }
        }
        for (V item : list) {
            if (item.getParentNodeId() != null && map.get(item.getParentNodeId()) != null) {
                map.get(item.getParentNodeId()).addChild(item);
            }
        }
        return map;
    }

    /**
     * 获取子树节点列表
     * @param root 子树根节点
     * @param <T> 树节点id类型
     * @param <V> 树节点类型
     * @return 子树节点列表
     */
    public static <T, V extends XSTreeable<T, V>> List<V> listSubTree(V root) {
        List<V> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        LinkedList<V> queue = new LinkedList<>();
        queue.add(root);
        V index = null;
        while ((index = queue.poll()) != null) {
            list.add(index);
            if (index.getChildren() != null) {
                for (V item : index.getChildren()) {
                    queue.addLast(item);
                }
            }
        }
        return list;
    }

    /**
     * 获取树节点路径
     * @param map 树节点哈希表
     * @param root 子树根节点
     * @param <T> 树节点id类型
     * @param <V> 树节点类型
     * @return 树节点路径
     */
    public static <T, V extends XSTreeable<T, V>> List<V> getTreePath(Map<T, V> map, V root) {
        List<V> list = new LinkedList<>();
        V index = root;
        while (index != null) {
            list.add(index);
            if (index.getParentNodeId() != null) {
                index = map.get(index.getParentNodeId());
            }
            else {
                index = null;
            }
        }
        Collections.reverse(list);
        return list;
    }

    /**
     * 获取子树列表
     * @param list 实现XSTreeable接口的对象列表
     * @param root 所有子树的根节点
     * @param <T> 树节点id类型
     * @param <V> 树节点类型
     * @return 子树列表
     */
    public static <T, V extends XSTreeable<T, V>> List<V> getSubTrees(List<V> list, V root) {
        List<V> trees = new ArrayList<>();
        if (root != null && root.getNodeId() != null) {
            for (V item : list) {
                boolean isChild = item.getParentNodeId()!= null && item.getParentNodeId().equals(root.getNodeId());
                if (isChild) {
                    trees.add(item);
                }
            }
        }
        else {
            for (V item : list) {
                if (item.getParentNodeId() == null) {
                    trees.add(item);
                }
            }
        }
        return trees;
    }
}
