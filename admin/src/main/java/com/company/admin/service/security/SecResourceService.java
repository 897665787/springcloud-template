package com.company.admin.service.security;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.security.SecResource;
import com.company.admin.entity.security.SecResourceFromXml;
import com.company.admin.entity.security.SecRole;
import com.company.admin.entity.security.SecStaff;
import com.company.admin.mapper.security.SecResourceDao;
import com.company.admin.springsecurity.UpdateAuthorityFilter;
import com.company.admin.util.ModelValidateUtil;
import com.company.admin.util.XSTreeUtil;
import com.company.framework.constant.CommonConstants;
import com.company.framework.globalresponse.ExceptionUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.digester.Digester;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 系统资源ServiceImpl
 * Created by JQ棣 on 2017/10/27.
 */
@Service
public class SecResourceService {

    private static final Logger logger = LoggerFactory.getLogger(SecResourceService.class);

    @Autowired
    private SecResourceDao secResourceDao;
    @Autowired
    private SecStaffService secStaffService;

    private Cache<String, Map<String, Map<String, SecResource>>> cache = null;

    @PostConstruct
    public void init() throws Exception {
        cache = CacheBuilder.newBuilder().expireAfterAccess(1800L, TimeUnit.SECONDS).maximumSize(1).build();
        try {
//            reload();
        } catch (Exception e) {}
        mapAll();
    }

    @Transactional
    public void save(SecResource secResource) {
        SecResource criteria = new SecResource();
        criteria.setKey(secResource.getKey());
        Long count = secResourceDao.count(criteria);
        if (count.compareTo(0L) > 0) {
            ExceptionUtil.throwException("资源已存在");
        }
        if (!secResource.getType().equals(2)) {
            //如果不是url资源则置空url和method
            secResource.setUrl(null);
            secResource.setMethod(null);
        }
        else {
            criteria = new SecResource();
            criteria.setUrl(secResource.getUrl());
            criteria.setMethod(secResource.getMethod());
            count = secResourceDao.count(criteria);
            if (count.compareTo(0L) > 0) {
                ExceptionUtil.throwException("资源已存在");
            }
        }
        //父级不可分配则子级不可分配
        if (secResource.getParent() != null && secResource.getParent().getId() != null
                && !secResource.getParent().getId().equals(0L)) {
            SecResource parent = secResourceDao.get(secResource.getParent());
            if (parent != null) {
                if (parent.getAssign().equals(0)) {
                    secResource.setAssign(0);
                }
            }
        }
        SecResource parent = secResourceDao.get(secResource.getParent());
        if (parent != null) {
            secResource.setParent(parent);
        }
        secResourceDao.save(secResource);
        secResourceDao.saveSuperiorRes(secResource);
        cache.invalidateAll();
    }

    @Transactional
    public void remove(SecResource secResource) {
        SecResource existent = get(secResource);
        SecResource children = new SecResource();
        children.setParent(existent);
        Long childrenCount = secResourceDao.count(children);
        if (childrenCount.compareTo(0L) > 0) {
            ExceptionUtil.throwException("资源被使用");
        }
        //校验是否存在角色拥有该资源
        Long roleCount = secResourceDao.countRole(existent);
        if (roleCount.compareTo(0L) > 0) {
            ExceptionUtil.throwException("资源被使用");
        }
        secResourceDao.removeSuperiorRes(existent);
        secResourceDao.remove(existent);
        cache.invalidateAll();
    }

    @Transactional
    public void update(SecResource secResource) {
        SecResource existent = get(secResource);
        if (secResource.getKey() != null) {
            SecResource criteria = new SecResource();
            criteria.setKey(secResource.getKey());
            List<SecResource> existents = secResourceDao.list(criteria);
            if (existents.size() > 0) {
                boolean isSelf = existents.get(0).getId().equals(existent.getId());
                if (!isSelf) {
                    ExceptionUtil.throwException("资源已存在");
                }
            }
        }
        if (secResource.getType() != null && secResource.getType().equals(2)) {
            SecResource criteria = new SecResource();
            criteria.setUrl(secResource.getUrl());
            criteria.setMethod(secResource.getMethod());
            List<SecResource> existents = secResourceDao.list(criteria);
            if (existents.size() > 0) {
                boolean isSelf = existents.get(0).getId().equals(existent.getId());
                if (!isSelf) {
                    ExceptionUtil.throwException("资源已存在");
                }
            }
        }
        SecResource parent = secResourceDao.get(secResource.getParent());
        if (parent != null) {
            secResource.setParent(parent);
        }
        secResourceDao.update(secResource);
        if (secResource.getAssign() != null) {
            List<SecResource> list = secResourceDao.listCombo(new SecResource());
            Map<Long, SecResource> map = XSTreeUtil.buildTree(list);
            SecResource latest = new SecResource();
            latest.setAssign(secResource.getAssign());
            //不可分配则所有子级也不可分配，可分配则直属父级和所有子级也可分配
            if (secResource.getAssign().equals(0)) {
                List<SecResource> subTreeList = XSTreeUtil.listSubTree(map.get(existent.getId()));
                latest.setList(subTreeList);
                //回收所有角色拥有的不可分配资源
                if (subTreeList.size() > 0) {
                    secResourceDao.batchRemoveRoleRes(latest);
                }
            }
            else {
                List<SecResource> treePath = XSTreeUtil.getTreePath(map, map.get(existent.getId()));
                latest.setList(treePath);
                List<SecResource> subTreeList = XSTreeUtil.listSubTree(map.get(existent.getId()));
                latest.getList().addAll(subTreeList);
            }
            secResourceDao.batchUpdate(latest);
        }
        cache.invalidateAll();
    }

    public SecResource get(SecResource secResource) {
        SecResource existent = secResourceDao.get(secResource);
        return existent;
    }

    public SecResource getByRequest(SecResource secResource) throws Exception {
        Map<String, SecResource> map = mapAll().get("request");
        return map.get(secResource.getUrl() + "_" + StringUtils.lowerCase(secResource.getMethod()));
    }

    public SecResource getByKey(SecResource secResource) throws Exception {
        Map<String, SecResource> map = mapAll().get("key");
        return map.get(secResource.getKey());
    }

    public XSPageModel<?> listAndCount(SecResource secResource) {
        secResource.setDefaultSort(new String[]{"r.id"}, new String[]{"DESC"});
        return XSPageModel.build(secResourceDao.list(secResource), secResourceDao.count(secResource));
    }

    public List<SecResource> tree(SecResource secResource) {
        secResource.setDefaultSort(new String[]{"type", "seq"}, new String[]{"ASC", "DESC"});
        List<SecResource> list = secResourceDao.listCombo(secResource);
        for (Iterator<SecResource> iterator = list.iterator(); iterator.hasNext();) {
            SecResource item = iterator.next();
            //不展示类型为url的资源
            if (item.getType().equals(2)) {
                iterator.remove();
            }
        }
        XSTreeUtil.buildTree(list);
        return XSTreeUtil.getSubTrees(list, new SecResource(0L));
    }

    public void invalidateCache() {
        cache.invalidateAll();
    }

    private Map<String, Map<String, SecResource>> mapAll() throws Exception {
        Map<String, Map<String, SecResource>> map = cache.get("cache", new Callable<Map<String, Map<String, SecResource>>>() {
            @Override
            public Map<String, Map<String, SecResource>> call() {
                logger.debug("Guava缓存未命中，从数据库中获取所有资源");
                List<SecResource> resourceList = secResourceDao.listAll();
                Map<String, Map<String, SecResource>> result = new HashMap<>();
                //构造以key为键的映射
                Map<String, SecResource> keyMap = new HashMap<>();
                for (SecResource item : resourceList) {
                    //构造逗号分隔的角色列表字符串
                    StringBuilder rolesStr = new StringBuilder();
                    for (SecRole role : item.getRoleList()) {
                        rolesStr.append("ROLE_" + String.valueOf(role.getId())).append(",");
                    }
                    if (rolesStr.length() > 0) {
                        rolesStr.deleteCharAt(rolesStr.length() - 1);
                    }
                    item.setRolesStr(rolesStr.toString());
                    keyMap.put(item.getKey(), item);
                }
                result.put("key", keyMap);
                //构造以url和method为键的映射
                Map<String, SecResource> requestMap = new HashMap<>();
                for (SecResource item : resourceList) {
                    if (item.getType().equals(2)) {
                        requestMap.put(item.getUrl() + "_" + StringUtils.lowerCase(item.getMethod()), item);
                    }
                }
                result.put("request", requestMap);
                return result;
            }
        });
        return map;
    }

    @Transactional
    public void reload() throws Exception {
        try {
            Digester digester = new Digester();
            digester.setValidating(false);

            //添加xml解析事件
            String modelClass = CommonConstants.BASE_PACKAGE + ".admin.entity.security.SecResourceFromXml";
            digester.addObjectCreate("resource", modelClass);
            digester.addSetProperties("resource");

            digester.addObjectCreate("resource/menu", modelClass);
            digester.addSetProperties("resource/menu");
            digester.addSetNext("resource/menu", "addMenu", modelClass);

            digester.addObjectCreate("resource/menu/menu", modelClass);
            digester.addSetProperties("resource/menu/menu");
            digester.addSetNext("resource/menu/menu", "addMenu", modelClass);

            digester.addObjectCreate("resource/function", modelClass);
            digester.addSetProperties("resource/function");
            digester.addSetNext("resource/function", "addFunction", modelClass);

            digester.addObjectCreate("resource/menu/function", modelClass);
            digester.addSetProperties("resource/menu/function");
            digester.addSetNext("resource/menu/function", "addFunction", modelClass);

            digester.addObjectCreate("resource/menu/menu/function", modelClass);
            digester.addSetProperties("resource/menu/menu/function");
            digester.addSetNext("resource/menu/menu/function", "addFunction", modelClass);

            digester.addObjectCreate("resource/api", modelClass);
            digester.addSetProperties("resource/api");
            digester.addSetNext("resource/api", "addApi", modelClass);

            digester.addObjectCreate("resource/menu/api", modelClass);
            digester.addSetProperties("resource/menu/api");
            digester.addSetNext("resource/menu/api", "addApi", modelClass);

            digester.addObjectCreate("resource/function/api", modelClass);
            digester.addSetProperties("resource/function/api");
            digester.addSetNext("resource/function/api", "addApi", modelClass);

            digester.addObjectCreate("resource/menu/menu/api", modelClass);
            digester.addSetProperties("resource/menu/menu/api");
            digester.addSetNext("resource/menu/menu/api", "addApi", modelClass);

            digester.addObjectCreate("resource/menu/function/api", modelClass);
            digester.addSetProperties("resource/menu/function/api");
            digester.addSetNext("resource/menu/function/api", "addApi", modelClass);

            digester.addObjectCreate("resource/menu/menu/function/api", modelClass);
            digester.addSetProperties("resource/menu/menu/function/api");
            digester.addSetNext("resource/menu/menu/function/api", "addApi", modelClass);

            //解析xml，结果存放在静态列表SecResourceFromXml.secResourceList
            digester.parse(new File(this.getClass().getResource("/").getPath() + "xsData.xml"));
            List<SecResourceFromXml> secResourceListFromXml = SecResourceFromXml.secResourceList;

            //校验数据格式
            for (SecResourceFromXml item : secResourceListFromXml) {
                Set<ConstraintViolation<SecResourceFromXml>> constraintViolations = ModelValidateUtil.getValidator().validate(item);
                for (ConstraintViolation<SecResourceFromXml> constraintViolation : constraintViolations) {
                    String errorMsg = "数据" + item.toString() + "出错，" + constraintViolation.getMessage();
                    ExceptionUtil.throwException(errorMsg);
                }
                if (item.getType().equals(2)) {
                    if (StringUtils.isBlank(item.getUrl())) {
                        String errorMsg = "数据" + item.toString() + "出错，接口地址不能为空";
                        ExceptionUtil.throwException(errorMsg);
                    }
                    if (StringUtils.isBlank(item.getMethod())) {
                        String errorMsg = "数据" + item.toString() + "出错，接口请求方式不能为空";
                        ExceptionUtil.throwException(errorMsg);
                    }
                }
            }

            //校验唯一性
            for (SecResourceFromXml secResourceFromXml : secResourceListFromXml) {
                for (SecResourceFromXml secResource : secResourceListFromXml) {
                    if (secResourceFromXml.equals(secResource)) {
                        continue;
                    }
                    if (secResourceFromXml.getKey().equals(secResource.getKey())) {
                        String errorMsg = "数据" + secResourceFromXml.toString() + "出错，键重复";
                        ExceptionUtil.throwException(errorMsg);
                    }
                    if (secResourceFromXml.getType().equals(2) && secResource.getType().equals(2)
                            && secResourceFromXml.getUrl().equals(secResource.getUrl())
                            && secResourceFromXml.getMethod().equals(secResource.getMethod())) {
                        String errorMsg = "数据" + secResourceFromXml.toString() + "出错，接口地址和接口请求方式重复";
                        ExceptionUtil.throwException(errorMsg);
                    }
                }
            }

            //从数据库查询所有资源，用来过滤资源和校验父id
            List<SecResourceFromXml> allUniqueInfo = secResourceDao.listAllUniqueInfo();

            //排除已经存在于数据库的资源，只增加新的资源，此处是为了保证数据安全性，若要修改或删除已存在的资源只能从管理后台操作
            for (Iterator<SecResourceFromXml> iterator = secResourceListFromXml.iterator(); iterator.hasNext();) {
                SecResourceFromXml secResourceFromXml = iterator.next();
                for (SecResourceFromXml secResource : allUniqueInfo) {
                    boolean sameKey = secResourceFromXml.getKey().equals(secResource.getKey());
                    boolean sameApi = secResourceFromXml.getType().equals(2) && secResource.getType().equals(2)
                            && secResourceFromXml.getUrl().equals(secResource.getUrl())
                            && secResourceFromXml.getMethod().equals(secResource.getMethod());
                    boolean existInDatabase = sameKey || sameApi;
                    if (existInDatabase) {
                        iterator.remove();
                    }
                }
            }

            //校验父键是否存在
            allUniqueInfo.addAll(secResourceListFromXml);
            for (SecResourceFromXml secResourceFromXml : secResourceListFromXml) {
                if (secResourceFromXml.getPkey() != null) {
                    boolean pkeyExist = false;
                    for (SecResourceFromXml secResource : allUniqueInfo) {
                        if (secResourceFromXml.equals(secResource)) {
                            continue;
                        }
                        if (secResourceFromXml.getPkey().equals(secResource.getKey())) {
                            pkeyExist = true;
                            break;
                        }
                    }
                    if (!pkeyExist) {
                        String errorMsg = "数据" + secResourceFromXml.toString() + "出错，pkey不存在";
                        ExceptionUtil.throwException(errorMsg);
                    }
                }
            }

            //更新资源并授予超级管理员
            if (secResourceListFromXml.size() > 0) {
                secResourceDao.saveMultiFromXml(secResourceListFromXml);
                secResourceDao.updateResPIdFromRes();
                secResourceDao.saveRoleRes(secResourceListFromXml);
            }
        } catch (Exception e) {
            logger.error("加载小梭数据配置文件失败，失败原因：", e);
            throw e;
        } finally {
            SecResourceFromXml.secResourceList.clear();
        }
        UpdateAuthorityFilter.lastUpdateAuthorityTime = LocalDateTime.now();
    }

    public void saveDynamicResource(String name, String key) {
        SecResource secResource = new SecResource();
        secResource.setName(name);
        secResource.setKey("dynamic_config_" + key);
        secResource.setParent(new SecResource(0L));
        secResource.setType(0);
        secResource.setSeq(0L);
        secResource.setDesc("动态数据资源");
        secResource.setAssign(1);
        secResource.setLog(0);
        save(secResource);

		SecStaff currentSecStaff = secStaffService.getByUsername(new SecStaff(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
		if (Objects.equals(currentSecStaff.getType(), 10)) {
			// 超级管理员，把新增资源修改到当前登录用户
			currentSecStaff.getResourceList().add(secResource);
		}
    }

    public void removeDynamicResource(String key) {
        SecResource existentRes = secResourceDao.getByKey("dynamic_config_" + key);
        remove(existentRes);

        SecStaff currentSecStaff = secStaffService.getByUsername(new SecStaff(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
		if (Objects.equals(currentSecStaff.getType(), 10)) {
			// 超级管理员，把删除资源修改到当前登录用户
			List<SecResource> resourceList = currentSecStaff.getResourceList();
			String _oldKey = "dynamic_config_" + key;
			Iterator<SecResource> iterator = resourceList.iterator();
			while(iterator.hasNext()){
				SecResource next = iterator.next();
				if (Objects.equals(next.getKey(), _oldKey)) {
					iterator.remove();
					break;
				}
			}
		}
    }

    public void updateDynamicResource(String name, String newKey, String key) {
        secResourceDao.updateByKey(name, "dynamic_config_" + newKey, "dynamic_config_" + key);

        SecStaff currentSecStaff = secStaffService.getByUsername(new SecStaff(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
		if (Objects.equals(currentSecStaff.getType(), 10)) {
			// 超级管理员，把修改资源修改到当前登录用户
			List<SecResource> resourceList = currentSecStaff.getResourceList();
			String _oldKey = "dynamic_config_" + key;
			String _newKey = "dynamic_config_" + newKey;
			for (SecResource secResource2 : resourceList) {
				if (Objects.equals(secResource2.getKey(), _oldKey)) {
					secResource2.setKey(_newKey);
					secResource2.setName(name);
					break;
				}
			}
		}
    }
}
