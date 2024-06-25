package com.company.framework.sequence.snowflake;

import com.company.framework.sequence.SequenceGenerator;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HutoolSnowflake implements SequenceGenerator {

	private final long datacenterIdBits = 5L;
	// 最大支持数据中心节点数0~31，一共32个
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

	private Snowflake snowflake = null;

	public HutoolSnowflake(int serverPort, long datacenterId) {
		String ip = NetUtil.getLocalhostStr();
		// 利用ip+端口保证每个服务的workerId不一致，用于ip一致或者端口一致的不同服务
		long workerId = (NetUtil.ipv4ToLong(ip) + serverPort) % (maxDatacenterId + 1);
		log.info("snowflake init,ip:{},port:{},datacenterId:{},workerId:{}", ip, serverPort, datacenterId,
				workerId);
		snowflake = new Snowflake(workerId, datacenterId);
	}

	@Override
	public long nextId() {
		return snowflake.nextId();
	}
}