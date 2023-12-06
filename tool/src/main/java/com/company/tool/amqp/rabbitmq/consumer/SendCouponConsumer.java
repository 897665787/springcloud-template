package com.company.tool.amqp.rabbitmq.consumer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

import com.company.common.util.JsonUtil;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.tool.api.enums.SubscribeEnum;
import com.company.tool.entity.SubscribeTemplateGrant;
import com.company.tool.service.SubscribeTemplateGrantService;
import com.company.tool.subscribe.AsyncSubscribeSender;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.feign.CouponFeign;
import com.company.user.api.feign.UserOauthFeign;
import com.company.user.api.response.UserCouponResp;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

/**
 * 发放优惠券消费者（订阅消息mq触发demo）
 */
@Slf4j
@Component
public class SendCouponConsumer {

	@Autowired
	private AsyncSubscribeSender asyncSubscribeSender;
	@Autowired
	private UserOauthFeign userOauthFeign;
	@Autowired
	private SubscribeTemplateGrantService subscribeTemplateGrantService;
	@Autowired
	private CouponFeign couponFeign;
	
	// 优惠券到账提醒、优惠券发放通知
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.SEND_COUPON.SUBSCRIBE_RECEIVE_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.SEND_COUPON.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void subscribeReceive(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				
			}
		});
	}
	
	// 优惠券使用提醒
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.SEND_COUPON.SUBSCRIBE_TOUSE_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.SEND_COUPON.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void subscribeTouse(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				Integer userCouponId = MapUtils.getInteger(params, "userCouponId");

				// 读取授权表，获取业务参数
				SubscribeTemplateGrant subscribeTemplateGrant = subscribeTemplateGrantService.selectById(1);
				String remark = subscribeTemplateGrant.getRemark();
				
				@SuppressWarnings("unchecked")
				Map<String, String> paramMap = JsonUtil.toEntity(remark, Map.class);
				
				Integer _userCouponId = MapUtils.getInteger(paramMap, "userCouponId");
				if (_userCouponId != null) {// 使用用户的优惠券判断
					if (!_userCouponId.equals(userCouponId)) {
						log.info("条件不满足{} {}", userCouponId, _userCouponId);
						return;
					}
				} else {// 使用优惠券模板判断
					Integer couponTemplateId = MapUtils.getInteger(paramMap, "couponTemplateId");
					Boolean isMatchTemplate = couponFeign.isMatchTemplate(userCouponId, couponTemplateId).dataOrThrow();
					if (!isMatchTemplate) {
						log.info("条件不满足{} {}", userCouponId, couponTemplateId);
						return;
					}
				}

				UserCouponResp userCouponResp = couponFeign.getUserCouponById(userCouponId).dataOrThrow();
				
				Integer userId = MapUtils.getInteger(params, "userId");
				String openid = userOauthFeign.selectIdentifier(userId, UserOauthEnum.IdentityType.WX_OPENID_MINIAPP)
						.dataOrThrow();
				
				// 优惠券过期前3天
				LocalDateTime planSendTime = userCouponResp.getEndTime().minusDays(3);
				String page = "";
				List<String> valueList = null;
				SubscribeEnum.Type type = SubscribeEnum.Type.COUPON_USE;
				LocalDateTime overTime = planSendTime.plusHours(1);
				// 创建订阅消息任务
				asyncSubscribeSender.send(openid, page, valueList, type, planSendTime, overTime);
			}
		});
	}
	
	// 优惠券过期提醒
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.SEND_COUPON.SUBSCRIBE_EXPIRE_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.SEND_COUPON.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void subscribeExpire(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				
			}
		});
	}
}