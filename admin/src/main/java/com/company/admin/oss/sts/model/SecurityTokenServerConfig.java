package com.company.admin.oss.sts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SecurityTokenServerConfig {
	@JsonProperty("AccessKeyID")
	private String AccessKeyID;
	@JsonProperty("AccessKeySecret")
	private String AccessKeySecret;
	@JsonProperty("RoleArn")
	private String RoleArn;
	@JsonProperty("TokenExpireTime")
	private Long TokenExpireTime;
	@JsonProperty("Policy")
	private Policy Policy;
	@JsonProperty("Endpoint")
	private String Endpoint;
	@JsonProperty("Region")
	private String Region;
	@JsonProperty("Bucket")
	private String Bucket;

	public String getAccessKeyID() {
		return this.AccessKeyID;
	}

	public void setAccessKeyID(String AccessKeyID) {
		this.AccessKeyID = AccessKeyID;
	}

	public String getAccessKeySecret() {
		return this.AccessKeySecret;
	}

	public void setAccessKeySecret(String AccessKeySecret) {
		this.AccessKeySecret = AccessKeySecret;
	}

	public String getRoleArn() {
		return this.RoleArn;
	}

	public void setRoleArn(String RoleArn) {
		this.RoleArn = RoleArn;
	}

	public long getTokenExpireTime() {
		return this.TokenExpireTime.longValue();
	}

	public void setTokenExpireTime(Long TokenExpireTime) {
		this.TokenExpireTime = TokenExpireTime;
	}

	public Policy getPolicy() {
		return this.Policy;
	}

	public void setPolicy(Policy policy) {
		this.Policy = policy;
	}

	public String getEndpoint() {
		return this.Endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.Endpoint = endpoint;
	}

	public String getRegion() {
		return this.Region;
	}

	public void setRegion(String region) {
		this.Region = region;
	}

	public String getBucket() {
		return this.Bucket;
	}

	public void setBucket(String bucket) {
		this.Bucket = bucket;
	}
}
