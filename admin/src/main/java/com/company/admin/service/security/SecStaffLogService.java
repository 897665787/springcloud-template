package com.company.admin.service.security;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.company.admin.aspect.Logable;
import com.company.admin.aspect.XSLogHandler;
import com.company.admin.aspect.XSLogger;
import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.security.SecResource;
import com.company.admin.entity.security.SecStaff;
import com.company.admin.entity.security.SecStaffLog;
import com.company.admin.mapper.security.SecStaffLogDao;
import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.framework.util.IpUtil;

/**
 * 系统用户日志ServiceImpl
 * Created by xuxiaowei on 2017/11/2.
 */
@Service
public class SecStaffLogService implements XSLogHandler, Logable {

    private static final Logger logger = LoggerFactory.getLogger(SecStaffLogService.class);

    @Autowired
    private SecStaffLogDao secStaffLogDao;

    @Autowired
    private SecResourceService secResourceService;
    
    @Autowired
    private SecStaffService secStaffService;

    public void save(SecStaffLog secStaffLog) {
        secStaffLogDao.save(secStaffLog);
    }

    public void remove(SecStaffLog secStaffLog) {
        SecStaffLog existent = get(secStaffLog);
        secStaffLogDao.remove(existent);
    }

    public SecStaffLog get(SecStaffLog secStaffLog) {
        SecStaffLog existent = secStaffLogDao.get(secStaffLog);
        if (existent == null) {
            throw new BusinessException("日志不存在");
        }
        return existent;
    }

    public XSPageModel<SecStaffLog> listAndCount(SecStaffLog secStaffLog) {
        secStaffLog.setDefaultSort("sl.id", "DESC");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) authentication.getPrincipal()).getUsername()));
            //非超级管理员不能查看超级管理员日志
            if (!secStaff.getType().equals(10)) {
                if (secStaffLog.getDynamic() == null) {
                    secStaffLog.setDynamic(new HashMap<>());
                }
                secStaffLog.getDynamic().put("notSuperior", "true");
            }
        }
        return XSPageModel.build(secStaffLogDao.list(secStaffLog), secStaffLogDao.count(secStaffLog));
    }

    @Override
    public void handle(XSLogger xsLogger) {
        if (xsLogger.getRequestUrl().contains("admin")) {
            try {
                SecResource criteria = new SecResource();
                criteria.setUrl(xsLogger.getRequestUrl().substring(xsLogger.getRequestUrl().indexOf("admin") - 1));
                criteria.setMethod(xsLogger.getRequestMethod());
                SecResource resource = secResourceService.getByRequest(criteria);
                if (resource != null && resource.getLog().equals(1)) {
                    SecStaffLog latest = new SecStaffLog();
                    SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
                    latest.setStaff(secStaff);
                    latest.setIp(xsLogger.getRequestIp());
                    latest.setTime(xsLogger.getRequestTime());
                    latest.setOperation(resource.getDesc());
                    latest.setStatus(xsLogger.getStatus() ? 1 : 0);
                    latest.setParameters(JsonUtil.toJsonString(xsLogger.getParameters()));
                    latest.setResult(xsLogger.getResult());
                    save(latest);
                }
            } catch (Exception e) {
                logger.error("error : ", e);
            }
        }
    }

    @Override
    public void log(HttpServletRequest request, String operation, Object param, Object result, boolean success) {
        if (request.getRequestURI().contains("admin")) {
            try {
                SecStaffLog latest = new SecStaffLog();
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null) {
                	SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) authentication).getUsername()));
                    latest.setStaff(secStaff);
                }
                latest.setIp(IpUtil.getRequestIp(request));
                latest.setTime(new Date());
                latest.setOperation(operation);
                latest.setStatus(success ? 1 : 0);
                latest.setParameters(JsonUtil.toJsonString(param));
                latest.setResult(JsonUtil.toJsonString(result));
                save(latest);
            } catch (Exception e) {
                logger.error("error : ", e);
            }
        }
    }
}
