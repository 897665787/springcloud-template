package com.company.app.easylogin.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.company.framework.cache.ICache;
import com.jqdi.easylogin.core.model.BindAuthCode;
import com.jqdi.easylogin.core.repository.OauthTempRepository;

@Component
@RequiredArgsConstructor
public class RedisTempOauthRepository implements OauthTempRepository {

	private final ICache cache;

	@Override
	public void saveBindAuthCode(String authcode, BindAuthCode bindAuthCode) {
		cache.set(authcode, bindAuthCode);
	}

	@Override
	public BindAuthCode getBindAuthCode(String authcode) {
		return cache.get(authcode, BindAuthCode.class);
	}

}