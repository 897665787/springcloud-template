package com.company.admin.springsecurity;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.company.admin.exception.ExceptionConsts;

/**
 * 系统用户请求权限校验Service
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> attributs)
            throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute item : attributs) {
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (item.getAttribute().equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException(ExceptionConsts.SEC_STAFF_ACCESS_DENY.getMessage());
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
