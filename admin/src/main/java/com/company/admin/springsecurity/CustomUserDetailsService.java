package com.company.admin.springsecurity;

import com.company.admin.entity.security.SecResource;
import com.company.admin.entity.security.SecStaff;
import com.company.admin.exception.ExceptionConsts;
import com.company.admin.service.security.SecStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 系统用户登录
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private SecStaffService secStaffService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SecStaff staff = null;
		try {
			staff = secStaffService.getByUsername(new SecStaff(username));
		} catch (Exception e) {
			throw new UsernameNotFoundException(ExceptionConsts.SEC_STAFF_NOT_EXIST.getMessage());
		}
		if (staff == null) {
			throw new UsernameNotFoundException(ExceptionConsts.SEC_STAFF_NOT_EXIST.getMessage());
		}
		if (!staff.isEnabled()) {
			throw new DisabledException(ExceptionConsts.SEC_STAFF_NOT_ENABLE.getMessage());
		}
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		for (SecResource resource : staff.getResourceList()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + resource.getKey()));
		}

		UserDetails user = new CustomUser(staff.getId(), staff.getNickname(), username, staff.getPassword(),
				staff.getStatus().equals(1), authorities);
		return user;
	}
}
