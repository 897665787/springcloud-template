package com.company.admin.service.security;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.annotation.XSTransactional;
import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.security.SecOrganization;
import com.company.admin.entity.security.SecRole;
import com.company.admin.mapper.security.SecOrganizationDao;
import com.company.admin.util.XSTreeUtil;
import com.company.common.exception.BusinessException;

/**
 * 系统组织ServiceImpl
 * Created by xuxiaowei on 2017/10/27.
 */
@Service
public class SecOrganizationService {

    @Autowired
    private SecOrganizationDao secOrganizationDao;

    public void save(SecOrganization secOrganization) {
        SecOrganization criteria = new SecOrganization();
        criteria.setName(secOrganization.getName());
        criteria.setParent(secOrganization.getParent());
        Long count = secOrganizationDao.count(criteria);
        if (count.compareTo(0L) > 0) {
            throw new BusinessException("组织已存在");
        }
        //父级禁用则子级禁用
        if (secOrganization.getParent() != null && secOrganization.getParent().getId() != null
                && !secOrganization.getParent().getId().equals(0L)) {
            SecOrganization parent = secOrganizationDao.get(secOrganization.getParent());
            if (parent != null) {
                if (parent.getStatus().equals(0)) {
                    secOrganization.setStatus(0);
                }
            }
        }
        secOrganizationDao.save(secOrganization);
    }

    @XSTransactional
    public void remove(SecOrganization secOrganization) {
        SecOrganization existent = get(secOrganization);
        SecOrganization children = new SecOrganization();
        children.setParent(secOrganization);
        Long childrenCount = secOrganizationDao.count(children);
        if (childrenCount.compareTo(0L) > 0) {
            throw new BusinessException("组织被使用");
        }
        //校验是否存在员工属于该组织
        Long staffCount = secOrganizationDao.countStaff(existent);
        if (staffCount.compareTo(0L) > 0) {
            throw new BusinessException("组织被使用");
        }
        secOrganizationDao.removeOrganizationRole(existent);
        secOrganizationDao.remove(existent);
    }

    @XSTransactional
    public void update(SecOrganization secOrganization) {
        SecOrganization existent = get(secOrganization);
        if (secOrganization.getName() != null || secOrganization.getParent() != null) {
            SecOrganization criteria = new SecOrganization();
            criteria.setName(secOrganization.getName() == null ? existent.getName() : secOrganization.getName());
            criteria.setParent(secOrganization.getParent() == null ? existent.getParent() : secOrganization.getParent());
            List<SecOrganization> existents = secOrganizationDao.list(criteria);
            if (existents.size() > 0) {
                boolean isSelf = existents.get(0).getId().equals(existent.getId());
                if (!isSelf) {
                    throw new BusinessException("组织已存在");
                }
            }
        }
        secOrganizationDao.update(secOrganization);
        if (secOrganization.getStatus() != null) {
            List<SecOrganization> list = secOrganizationDao.listCombo(new SecOrganization());
            Map<Long, SecOrganization> map = XSTreeUtil.buildTree(list);
            SecOrganization latest = new SecOrganization();
            latest.setStatus(secOrganization.getStatus());
            //禁用则所有子级也禁用，启用则直属父级和所有子级也启用
            if (secOrganization.getStatus().equals(0)) {
                List<SecOrganization> subTreeList = XSTreeUtil.listSubTree(map.get(existent.getId()));
                latest.setList(subTreeList);
            }
            else {
                List<SecOrganization> treePath = XSTreeUtil.getTreePath(map, map.get(existent.getId()));
                latest.setList(treePath);
                List<SecOrganization> subTreeList = XSTreeUtil.listSubTree(map.get(existent.getId()));
                latest.getList().addAll(subTreeList);
            }
            secOrganizationDao.batchUpdate(latest);
        }
    }

    public SecOrganization get(SecOrganization secOrganization) {
        SecOrganization existent = secOrganizationDao.get(secOrganization);
        if (existent == null) {
            throw new BusinessException("组织不存在");
        }
        return existent;
    }

    public XSPageModel<?> listAndCount(SecOrganization secOrganization) {
        secOrganization.setDefaultSort("id", "DESC");
        return XSPageModel.build(secOrganizationDao.list(secOrganization), secOrganizationDao.count(secOrganization));
    }

    public List<SecOrganization> tree(SecOrganization secOrganization) {
        List<SecOrganization> list = secOrganizationDao.listCombo(secOrganization);
        XSTreeUtil.buildTree(list);
        return XSTreeUtil.getSubTrees(list, new SecOrganization(0L));
    }

    public List<SecRole> listRole(SecOrganization secOrganization) {
        SecOrganization existent = get(secOrganization);
        List<SecRole> roleList = secOrganizationDao.listRoleCombo(new SecRole());
        List<SecRole> ownedRoleList = secOrganizationDao.listRole(existent);
        for (Iterator<SecRole> iterator = roleList.iterator(); iterator.hasNext();) {
            SecRole item = iterator.next();
            for (SecRole owned : ownedRoleList) {
                if (item.getId().equals(owned.getId())) {
                    item.setChecked(1);
                    break;
                }
            }
        }
        return roleList;
    }

    @XSTransactional
    public void authorizeRole(SecOrganization secOrganization) {
        SecOrganization existent = get(secOrganization);
        //不能授予超级管理员角色
        for (Iterator<SecRole> iterator = secOrganization.getRoleList().iterator(); iterator.hasNext();) {
            if (iterator.next().getId().equals(1L)) {
                iterator.remove();
            }
        }
        secOrganizationDao.removeOrganizationRole(existent);
        if (secOrganization.getRoleList().size() > 0) {
            secOrganizationDao.saveOrganizationRole(secOrganization);
        }
    }
}
