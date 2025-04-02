package com.company.admin.mapper.security;

import com.company.admin.entity.security.SecOrganization;
import com.company.admin.entity.security.SecRole;

import java.util.List;

/**
 * 系统组织Dao
 * Created by JQ棣 on 2017/10/27.
 */
public interface SecOrganizationDao {

    int save(SecOrganization secOrganization);

    int saveOrganizationRole(SecOrganization secOrganization);

    int remove(SecOrganization secOrganization);

    int removeOrganizationRole(SecOrganization secOrganization);

    int update(SecOrganization secOrganization);

    int batchUpdate(SecOrganization secOrganization);

    SecOrganization get(SecOrganization secOrganization);

    List<SecOrganization> list(SecOrganization secOrganization);

    List<SecOrganization> listCombo(SecOrganization secOrganization);

    /**
     * 获取角色列表
     * @param secRole
     * @return 角色列表
     */
    List<SecRole> listRoleCombo(SecRole secRole);

    /**
     * 获取属于指定组织的角色列表
     * @param secOrganization 参数id
     * @return 角色列表
     */
    List<SecRole> listRole(SecOrganization secOrganization);

    Long count(SecOrganization secOrganization);

    /**
     * 获取拥有指定组织的员工数量
     * @param secOrganization 参数id
     * @return 员工数量
     */
    Long countStaff(SecOrganization secOrganization);
}
