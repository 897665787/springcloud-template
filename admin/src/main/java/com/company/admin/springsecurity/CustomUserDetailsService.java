package com.company.admin.springsecurity;

import com.company.admin.entity.security.SecResource;
import com.company.admin.entity.security.SecStaff;
import com.company.admin.service.security.SecStaffService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final SecStaffService secStaffService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SecStaff staff = null;
		try {
			staff = secStaffService.getByUsername(new SecStaff(username));
		} catch (Exception e) {
			throw new UsernameNotFoundException("员工不存在");
		}
		if (staff == null) {
			throw new UsernameNotFoundException("员工不存在");
		}
		if (!staff.isEnabled()) {
			throw new DisabledException("员工被禁用");
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
