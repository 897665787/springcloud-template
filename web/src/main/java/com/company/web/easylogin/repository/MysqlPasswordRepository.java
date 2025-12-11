package com.company.web.easylogin.repository;

import com.company.user.api.feign.UserOauthFeign;
import com.company.web.util.PassWordUtil;
import com.jqdi.easylogin.core.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MysqlPasswordRepository implements PasswordRepository {
	@Autowired
	private UserOauthFeign userOauthFeign;

	@Override
	public boolean checkPassword(String userId, String password) {
        String passwordDB = userOauthFeign.selectPassword(Integer.valueOf(userId)).dataOrThrow();
		String md5Password = PassWordUtil.md5(password);
		return md5Password.equals(passwordDB);
	}
}