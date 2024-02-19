package com.company.user.amqp.rabbitmq.consumer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.commons.collections4.MapUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.framework.cache.ICache;
import com.company.user.entity.UserSource;
import com.company.user.mapper.user.UserSourceMapper;
import com.rabbitmq.client.Channel;

import cn.hutool.core.date.LocalDateTimeUtil;

@Component
public class UserSourceConsumer {

	private static final String EXIST_VALUE = "1";

	@Autowired
	private ICache cache;
	@Autowired
	private UserSourceMapper userSourceMapper;

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.USER_SOURCE.SOURCE_RECORD_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.USER_SOURCE.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void loginRecord(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				String source = MapUtils.getString(params, "source");
				String deviceid = MapUtils.getString(params, "deviceid");

				// 数据量可能很大，需要快速过滤重复的数据，加快处理速度
				String key = String.format("user_source:%s:%s", source, deviceid);
				String result = cache.get(key);
				if (EXIST_VALUE.equals(result)) {
					return;
				}

				// 保存source、deviceid关联关系到DB
				String timeStr = MapUtils.getString(params, "time");
				LocalDateTime time = LocalDateTimeUtil.parse(timeStr,
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				save2db(deviceid, source, time);

				// 数据量可能很大，需要快速过滤重复的数据，加快处理速度
				cache.set(key, EXIST_VALUE, 30, TimeUnit.MINUTES);
			}
		});
	}

	private void save2db(String deviceid, String source, LocalDateTime time) {
		// 获取deviceid最近1次记录
		UserSource lastUserSource = userSourceMapper.selectLastByDeviceid(deviceid);
		if (lastUserSource != null) {
			if (source.equals(lastUserSource.getSource())) {
				return;
			}
		}

		userSourceMapper.saveOrIgnore(deviceid, source, time);
	}
}