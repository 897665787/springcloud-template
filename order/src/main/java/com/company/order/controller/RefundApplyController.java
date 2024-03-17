package com.company.order.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.order.api.constant.Constants;
import com.company.order.api.enums.PayRefundApplyEnum;
import com.company.order.api.feign.PayFeign;
import com.company.order.api.feign.RefundApplyFeign;
import com.company.order.api.request.PayRefundApplyReq;
import com.company.order.api.request.PayRefundReq;
import com.company.order.api.request.RefundNotifyReq;
import com.company.order.entity.PayRefundApply;
import com.company.order.mapper.PayRefundApplyMapper;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/refundApply")
public class RefundApplyController implements RefundApplyFeign {

	@Autowired
	private PayRefundApplyMapper payRefundApplyMapper;

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private PayFeign payFeign;
	
	@Autowired
	private ThreadPoolTaskExecutor executor;
	
	/**
	 * 退款申请
	 * 
	 * <pre>
	 * 如果需要获得退款结果，请订阅 FanoutConstants.REFUND_APPLY_RESULT 的广播消息
	 * 参考：RefundApplyResultConsumer
	 * </pre>
	 * 
	 * @param payRefundApplyReq
	 * @return
	 */
	@Override
	public Result<Integer> refundApply(@RequestBody @Valid PayRefundApplyReq payRefundApplyReq) {
		PayRefundApply payRefundApply = new PayRefundApply();
		payRefundApply.setOrderCode(payRefundApplyReq.getOrderCode());
		payRefundApply.setOldOrderCode(payRefundApplyReq.getOldOrderCode());
		payRefundApply.setAmount(payRefundApplyReq.getAmount());
		payRefundApply.setBusinessType(payRefundApplyReq.getBusinessType().getCode());
		payRefundApply.setVerifyStatus(payRefundApplyReq.getVerifyStatus().getCode());
		payRefundApply.setRefundStatus(PayRefundApplyEnum.RefundStatus.NO_REFUND.getCode());
		payRefundApply.setReason(payRefundApplyReq.getReason());
		payRefundApply.setAttach(payRefundApplyReq.getAttach());
		payRefundApply.setRemark(payRefundApplyReq.getRemark());

		payRefundApplyMapper.insert(payRefundApply);
		return Result.success(payRefundApply.getId());
	}
	
	@Override
	public Result<List<Integer>> selectId4Deal() {
		List<Integer> idList = payRefundApplyMapper.selectId4Deal(PayRefundApplyEnum.RefundStatus.NO_REFUND,
				PayRefundApplyEnum.VerifyStatus.PASS, PayRefundApplyEnum.VerifyStatus.REJECT);
		return Result.success(idList);
	}

	/**
	 * 处理退款申请
	 * 
	 * <pre>
	 * 申请结果或退款结果会以广播方式通知到各业务
	 * </pre>
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Result<Boolean> dealRefundApply(@RequestParam("id") Integer id) {
		PayRefundApply payRefundApply = payRefundApplyMapper.selectById(id);
		if (PayRefundApplyEnum.VerifyStatus
				.of(payRefundApply.getVerifyStatus()) == PayRefundApplyEnum.VerifyStatus.WAIT_VERIFY) {
			// ‘待审核’的数据不处理
			return Result.fail("‘待审核’的数据不处理");
		}
		
		if (PayRefundApplyEnum.RefundStatus
				.of(payRefundApply.getRefundStatus()) == PayRefundApplyEnum.RefundStatus.REJECT
				|| PayRefundApplyEnum.RefundStatus
						.of(payRefundApply.getRefundStatus()) == PayRefundApplyEnum.RefundStatus.APPLY_FAIL
				|| PayRefundApplyEnum.RefundStatus
						.of(payRefundApply.getRefundStatus()) == PayRefundApplyEnum.RefundStatus.REFUND_SCUESS
				|| PayRefundApplyEnum.RefundStatus
						.of(payRefundApply.getRefundStatus()) == PayRefundApplyEnum.RefundStatus.REFUND_FAIL) {
			// ‘END状态’的数据不处理
			return Result.fail("‘END状态’的数据不处理");
		}

		String remark = Utils.rightRemark(payRefundApply.getRemark(), PayRefundApplyEnum.RefundStatus.DEALING.getDesc());
		payRefundApplyMapper.updateRefundStatusRemarkById(PayRefundApplyEnum.RefundStatus.DEALING, remark,
				payRefundApply.getId());

		if (PayRefundApplyEnum.VerifyStatus
				.of(payRefundApply.getVerifyStatus()) == PayRefundApplyEnum.VerifyStatus.REJECT) {
			// 审核驳回
			remark = Utils.rightRemark(remark, PayRefundApplyEnum.RefundStatus.DEALING.getDesc());
			payRefundApplyMapper.updateRefundStatusRemarkById(PayRefundApplyEnum.RefundStatus.REJECT, remark,
					payRefundApply.getId());
			
			orderRefundApplyEventMessage(false, "审核驳回", payRefundApply.getOldOrderCode(),
					payRefundApply.getOrderCode(), payRefundApply.getAttach(), null, null, null);
			return Result.success(Boolean.TRUE);
		}

		BigDecimal amount = payRefundApply.getAmount();
		if (amount.compareTo(BigDecimal.ZERO) == 0) {// 退款金额为0，不用走支付退款逻辑，直接认为是申请退款成功
			// 申请退款成功
			remark = Utils.rightRemark(remark, PayRefundApplyEnum.RefundStatus.APPLY_SCUESS.getDesc());
			payRefundApplyMapper.updateRefundStatusRemarkById(PayRefundApplyEnum.RefundStatus.APPLY_SCUESS, remark,
					payRefundApply.getId());
			
			executor.submit(() -> {
				RefundNotifyReq refundNotifyReq = new RefundNotifyReq();
				refundNotifyReq.setSuccess(true);
				refundNotifyReq.setMessage("0元退款，跳过支付退款流程");
				refundNotifyReq.setOrderCode(payRefundApply.getOldOrderCode());
				refundNotifyReq.setRefundOrderCode(payRefundApply.getOrderCode());
				refundNotifyReq.setAttach(payRefundApply.getAttach());
				refundNotifyReq.setThisRefundAmount(BigDecimal.ZERO);
				refundNotifyReq.setTotalRefundAmount(BigDecimal.ZERO);
				refundNotifyReq.setRefundAll(true);
				
				Result<Void> refundNotifyResult = this.refundNotify(refundNotifyReq);
				log.info("refundNotify:{}", JsonUtil.toJsonString(refundNotifyResult));
				// 后续逻辑 ----------> refundNotify
			});
			return Result.success(Boolean.TRUE);
		}

		PayRefundReq payRefundReq = new PayRefundReq();
		payRefundReq.setRefundOrderCode(payRefundApply.getOldOrderCode());// 原订单号
		payRefundReq.setOrderCode(payRefundApply.getOrderCode());// 退款订单号
		payRefundReq.setNotifyUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/refundApply/refundNotify");
		payRefundReq.setRefundRemark(payRefundApply.getReason());
		payRefundReq.setAttach(payRefundApply.getAttach());
		payRefundReq.setRefundAmount(amount);

		Result<Void> result = payFeign.refund(payRefundReq);
		if (!result.successCode()) {
			// 申请退款失败
			remark = Utils.rightRemark(remark, result.getMessage());
			payRefundApplyMapper.updateRefundStatusRemarkById(PayRefundApplyEnum.RefundStatus.APPLY_FAIL, remark,
					payRefundApply.getId());
			orderRefundApplyEventMessage(false, result.getMessage(), payRefundApply.getOldOrderCode(),
					payRefundApply.getOrderCode(), payRefundApply.getAttach(), null, null, null);
			return Result.fail("申请退款失败");
		}
		// 申请退款成功
		remark = Utils.rightRemark(remark, PayRefundApplyEnum.RefundStatus.APPLY_SCUESS.getDesc());
		payRefundApplyMapper.updateRefundStatusRemarkById(PayRefundApplyEnum.RefundStatus.APPLY_SCUESS, remark,
				payRefundApply.getId());
		return Result.success(Boolean.TRUE);
		// 后续逻辑 ----------> refundNotify
	}

	/**
	 * 退款回调(使用restTemplate的方式调用)
	 * 
	 * @param refundNotifyReq
	 * @return
	 */
	@PostMapping("/refundNotify")
	public Result<Void> refundNotify(@RequestBody RefundNotifyReq refundNotifyReq) {
		String refundOrderCode = refundNotifyReq.getRefundOrderCode();
		PayRefundApply payRefundApply = payRefundApplyMapper.selectByOrderCode(refundOrderCode);

		if (!refundNotifyReq.getSuccess()) {
			// 退款失败
			String remark = Utils.rightRemark(payRefundApply.getRemark(), refundNotifyReq.getMessage());
			payRefundApplyMapper.updateRefundStatusRemarkById(PayRefundApplyEnum.RefundStatus.REFUND_FAIL, remark,
					payRefundApply.getId());
			orderRefundApplyEventMessage(false, refundNotifyReq.getMessage(), refundNotifyReq.getOrderCode(),
					refundOrderCode, refundNotifyReq.getAttach(), refundNotifyReq.getThisRefundAmount(),
					refundNotifyReq.getTotalRefundAmount(), refundNotifyReq.getRefundAll());
			return Result.success();
		}

		// 退款成功
		String remark = Utils.rightRemark(payRefundApply.getRemark(), PayRefundApplyEnum.RefundStatus.REFUND_SCUESS.getDesc());
		payRefundApplyMapper.updateRefundStatusRemarkById(PayRefundApplyEnum.RefundStatus.REFUND_SCUESS, remark,
				payRefundApply.getId());
		orderRefundApplyEventMessage(true, refundNotifyReq.getMessage(), refundNotifyReq.getOrderCode(),
				refundOrderCode, refundNotifyReq.getAttach(), refundNotifyReq.getThisRefundAmount(),
				refundNotifyReq.getTotalRefundAmount(), refundNotifyReq.getRefundAll());
		return Result.success();
	}
	
    /**
	 * 订单退款申请结果事件发布消息
	 */
	public void orderRefundApplyEventMessage(Boolean success, String message, String orderCode, String refundOrderCode,
			String attach, BigDecimal thisRefundAmount, BigDecimal totalRefundAmount, Boolean refundAll) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("success", success);
		params.put("message", message);
		params.put("orderCode", orderCode);
		params.put("refundOrderCode", refundOrderCode);
		params.put("attach", attach);
		params.put("thisRefundAmount", thisRefundAmount);
		params.put("totalRefundAmount", totalRefundAmount);
		params.put("refundAll", refundAll);
		
		messageSender.sendFanoutMessage(params, FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE);
	}
}