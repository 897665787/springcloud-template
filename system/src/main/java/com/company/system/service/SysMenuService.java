package com.company.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.framework.util.PropertyUtils;
import com.company.system.dto.MenuTreeNode;
import com.company.system.entity.SysMenu;
import com.company.system.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu>
		implements IService<SysMenu> {

	public Set<String> listPermsByMenuIds(Set<Integer> menuIds) {
		return baseMapper.listPermsByMenuIds(menuIds);
	}

    public List<SysMenu> getByUserId(Integer userId) {
        return baseMapper.selectByUserId(userId);
    }

    public List<MenuTreeNode> getTree(Integer userId, boolean isAdmin) {
        List<SysMenu> menuList;
        if (isAdmin) {
            menuList = baseMapper.selectAllOn();
        } else {
            menuList = baseMapper.selectByUserId(userId);
        }
        return buildTree(menuList, 0);
    }

    public List<MenuTreeNode> buildTree(List<SysMenu> menuList, int parentId) {
        List<MenuTreeNode> nodeList = PropertyUtils.copyArrayProperties(menuList, MenuTreeNode.class);
        List<MenuTreeNode> ret = new ArrayList<>();
        for (MenuTreeNode node : nodeList) {
            if (node.getParentId() == parentId) {
                recursionFn(nodeList, node);
                ret.add(node);
            }
        }
        return ret;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param node    子节点
     */
    private void recursionFn(List<MenuTreeNode> list, MenuTreeNode node) {
        // 得到子节点列表
        List<MenuTreeNode> childList = list.stream().filter(v -> v.getParentId().equals(node.getId())).collect(Collectors.toList());
        node.setChildren(childList);
        for (MenuTreeNode child : childList) {
            recursionFn(list, child);
        }
    }

}
