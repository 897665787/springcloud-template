package com.company.admin.mapper.security;

import com.company.admin.entity.security.SecResource;
import com.company.admin.entity.security.SecResourceFromXml;
import com.company.admin.entity.security.SecRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统资源Dao
 * Created by xuxiaowei on 2017/10/27.
 */
public interface SecResourceDao {

    int save(SecResource secResource);

    /**
     * 赋予超级管理员指定资源
     * @param secResource 参数id
     * @return 受影响行数
     */
    int saveSuperiorRes(SecResource secResource);

    int remove(SecResource secResource);

    /**
     * 删除超级管理员指定资源
     * @param secResource 参数id
     * @return 受影响行数
     */
    int removeSuperiorRes(SecResource secResource);

    /**
     * 批量删除指定资源和角色的映射
     * @param secResource
     * @return 受影响行数
     */
    int batchRemoveRoleRes(SecResource secResource);

    int update(SecResource secResource);

    int batchUpdate(SecResource secResource);

    SecResource get(SecResource secResource);

    List<SecResource> list(SecResource secResource);

    List<SecResource> listCombo(SecResource secResource);
    
	List<SecResource> listComboByStaffId(@Param("staffId") String staffId);

    /**
     * 获取所有资源和其所属的角色列表
     * @return 资源列表
     */
    List<SecResource> listAll();

    Long count(SecResource secResource);

    /**
     * 获取拥有指定资源的角色数量
     * @param secResource 参数id
     * @return 角色数量
     */
    Long countRole(SecResource secResource);

    List<SecResourceFromXml> listAllUniqueInfo();

    int saveMultiFromXml(List<SecResourceFromXml> secResourceFromXmlList);

    int updateResPIdFromRes();

    int saveRoleRes(List<SecResourceFromXml> secResourceFromXmlList);

    List<SecResource> listByRoles(@Param("roles") List<SecRole> roles);

    SecResource getByKey(String key);

    int updateByKey(@Param("name") String name, @Param("newKey") String newKey, @Param("key") String key);
}
