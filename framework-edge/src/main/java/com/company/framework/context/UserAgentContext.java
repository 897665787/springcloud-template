package com.company.framework.context;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class UserAgentContext {
	String platform;// 平台：app(APP)、mini(微信小程序)、h5(H5页面)、alimini(支付宝小程序)、alipaymini(支付宝小程序内H5)
	String operator;// 操作系统：ios(iOS)、mac(iOS)、android(Android)、win(Windows,如果获取不到可以不要)、devtools(小程序开发工具)
	String channel;// 渠道：wx(微信小程序)、ali(支付宝小程序)、ios(苹果应用商店)、xiaomi(小米应用商店)、huawei(华为应用商店)等

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		if (StringUtils.isBlank(this.platform)) {// 先设置优先级高
			this.platform = platform;
		}
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		if (StringUtils.isBlank(this.operator)) {// 先设置优先级高
			this.operator = operator;
		}
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		if (StringUtils.isBlank(this.channel)) {// 先设置优先级高
			this.channel = channel;
		}
	}

}
