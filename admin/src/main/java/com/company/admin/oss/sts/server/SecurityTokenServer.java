package com.company.admin.oss.sts.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.company.admin.oss.sts.model.SecurityToken;
import com.company.admin.oss.sts.model.SecurityTokenServerConfig;
import com.company.common.util.JsonUtil;

public class SecurityTokenServer {
	private static final String REGION_CN_HANGZHOU = "cn-hangzhou";
	private static final String STS_API_VERSION = "2015-04-01";
	private SecurityTokenServerConfig config;
	private Long updateTime = null;
	private SecurityToken token = new SecurityToken();
	private String configFileName;
	private String sessionName = "default";
	private String region = REGION_CN_HANGZHOU;

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	@PostConstruct
	public void init() throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(this.configFileName)));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		this.config = JsonUtil.toEntity(builder.toString(), SecurityTokenServerConfig.class);
		if (this.config.getRegion() != null) {
			this.region = this.config.getRegion();
		}
	}

	public synchronized SecurityToken getToken() {
		if (invalid()) {
			requestSecurityToken();
		}
		return this.token;
	}

	private boolean invalid() {
		if (this.updateTime == null) {
			return true;
		}
		Long currentTime = Long.valueOf(System.currentTimeMillis());

		return (currentTime.longValue() - this.updateTime.longValue()) / 1000L > this.config.getTokenExpireTime()
				- 300L;
	}

	private void requestSecurityToken() {
		try {
			IClientProfile profile = DefaultProfile.getProfile(this.region, this.config.getAccessKeyID(),
					this.config.getAccessKeySecret());
			DefaultAcsClient client = new DefaultAcsClient(profile);

			AssumeRoleRequest request = new AssumeRoleRequest();
			request.setVersion(STS_API_VERSION);
			request.setMethod(MethodType.POST);

			request.setProtocol(ProtocolType.HTTPS);

			request.setRoleArn(this.config.getRoleArn());

			request.setRoleSessionName(this.sessionName);
			request.setPolicy(JsonUtil.toJsonString(this.config.getPolicy()));
			request.setDurationSeconds(Long.valueOf(this.config.getTokenExpireTime()));

			AssumeRoleResponse stsResponse = (AssumeRoleResponse) client.getAcsResponse(request);

			this.token.setCode("success");
			this.token.setAccessKeyId(stsResponse.getCredentials().getAccessKeyId());
			this.token.setAccessKeySecret(stsResponse.getCredentials().getAccessKeySecret());
			this.token.setSecurityToken(stsResponse.getCredentials().getSecurityToken());
			this.token.setExpiration(stsResponse.getCredentials().getExpiration());

			this.token.setBucket(this.config.getBucket());
			this.token.setEndpoint(this.config.getEndpoint());
			this.token.setRegion(this.config.getRegion());

			this.updateTime = Long.valueOf(System.currentTimeMillis());
		} catch (ClientException e) {
			this.token.setMsg(e.getErrMsg());
			this.token.setCode(e.getErrCode());
			this.token.setAccessKeyId("");
			this.token.setAccessKeySecret("");
			this.token.setSecurityToken("");
			this.token.setExpiration("");

			this.token.setBucket("");
			this.token.setEndpoint("");
			this.token.setRegion("");

			this.updateTime = null;
		}
	}
}
