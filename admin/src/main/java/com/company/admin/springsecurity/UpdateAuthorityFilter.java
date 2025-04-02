package com.company.admin.springsecurity;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.admin.entity.security.SecStaff;
import com.company.admin.service.security.SecStaffService;

/**
 * @author JQ棣
 * @date 2018/10/3
 */
@Component
public class UpdateAuthorityFilter extends OncePerRequestFilter {

    public static LocalDateTime lastUpdateAuthorityTime;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecStaffService secStaffService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        LocalDateTime lastRequestTime = (LocalDateTime) httpServletRequest.getSession().getAttribute("lastRequestTime");
        httpServletRequest.getSession().setAttribute("lastRequestTime", LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean updateAuthority = lastUpdateAuthorityTime != null && lastRequestTime != null && authentication != null
                && lastRequestTime.isBefore(lastUpdateAuthorityTime);
        if (updateAuthority) {
        	SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) authentication.getPrincipal()).getUsername()));
            UserDetails newSecStaff = userDetailsService.loadUserByUsername(secStaff.getUsername());
            UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
                    newSecStaff, authentication.getCredentials(), newSecStaff.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
