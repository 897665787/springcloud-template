package com.company.admin.mapper.security;

import com.company.admin.entity.security.SecOrganization;
import com.company.admin.entity.security.SecRole;
import com.company.admin.entity.security.SecStaff;

import java.util.List;

/**
 * 系统用户Dao
 * Created by JQ棣 on 2017/10/27.
 */
public interface SecStaffDao {

    int save(SecStaff secStaff);

    int saveStaffRole(SecStaff secStaff);

    int saveStaffOrganization(SecStaff secStaff);

    int remove(SecStaff secStaff);

    int removeStaffRole(SecStaff secStaff);

    int removeStaffOrganization(SecStaff secStaff);

    int update(SecStaff secStaff);

    SecStaff get(SecStaff secStaff);

    SecStaff getByUsername(SecStaff secStaff);

    SecStaff getByUsernameWithoutType(String username);

    SecStaff getByMobileWithoutType(String mobile);

    List<SecStaff> list(SecStaff secStaff);

    /**
     * 获取角色列表
     * @param secRole
     * @return 角色列表
     */
    List<SecRole> listRoleCombo(SecRole secRole);

    /**
     * 获取属于指定员工的角色列表
     * @param secStaff 参数id
     * @return 角色列表
     */
    List<SecRole> listRole(SecStaff secStaff);

    /**
     * 获取属于指定多个组织的角色列表
     * @param secStaff 参数organizationList
     * @return 角色列表
     */
    List<SecRole> listRoleByOrganization(SecStaff secStaff);

    /**
     * 获取组织列表
     * @param secOrganization
     * @return 组织列表
     */
    List<SecOrganization> listOrganizationCombo(SecOrganization secOrganization);

    /**
     * 获取属于指定员工的组织列表
     * @param secStaff 参数id
     * @return 组织列表
     */
    List<SecOrganization> listOrganization(SecStaff secStaff);

    Long count(SecStaff secStaff);
}
