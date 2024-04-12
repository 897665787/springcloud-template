package com.company.order.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.order.api.enums.PayRefundApplyEnum;
import com.company.order.entity.PayRefundApply;

public interface PayRefundApplyMapper extends BaseMapper<PayRefundApply> {
	
	@Select("select * from bu_pay_refund_apply where order_code = #{orderCode}")
	PayRefundApply selectByOrderCode(@Param("orderCode") String orderCode);
	
	@Select("select * from bu_pay_refund_apply where old_order_code = #{oldOrderCode} order by id desc limit 1")
	PayRefundApply selectLastByOldOrderCode(@Param("oldOrderCode") String oldOrderCode);
	
	@Select("update bu_pay_refund_apply set status = #{status} where id = #{id}")
	Integer updateVerifyStatusById(@Param("status") Integer status, @Param("id") Integer id);
	
//	@Select("update bu_pay_refund_apply set refund_status = #{refundStatus.code},remark = #{remark} where id = #{id}")
//	Integer updateRefundStatusById(@Param("status") PayRefundApplyEnum.RefundStatus refundStatus, @Param("id") Integer id);

	@Select("update bu_pay_refund_apply set refund_status = #{refundStatus.code},remark = #{remark} where id = #{id}")
	Integer updateRefundStatusRemarkById(@Param("refundStatus") PayRefundApplyEnum.RefundStatus refundStatus,
			@Param("remark") String remark, @Param("id") Integer id);
	
//	@Update("update bu_inner_callback set status = #{status.code} where id = #{id}")
//	int abandonCallback(@Param("status") InnerCallbackEnum.Status abandonCallback, @Param("id") Integer id);

	@Select("select id from bu_pay_refund_apply where refund_status = #{noRefundStatus.code} and verify_status in(#{passStatus.code},#{rejectStatus.code})")
	List<Integer> selectId4Deal(@Param("noRefundStatus") PayRefundApplyEnum.RefundStatus noRefundStatus,
			@Param("passStatus") PayRefundApplyEnum.VerifyStatus passStatus,
			@Param("rejectStatus") PayRefundApplyEnum.VerifyStatus rejectStatus);

	@Select("select ifnull(sum(amount), 0) from bu_pay_refund_apply where old_order_code = #{orderCode} and refund_status in (31, 51)")
	BigDecimal selectRefundAmount(String orderCode);
}