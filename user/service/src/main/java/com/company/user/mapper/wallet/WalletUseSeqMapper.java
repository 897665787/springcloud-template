package com.company.user.mapper.wallet;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.api.enums.WalletEnum;
import com.company.user.entity.WalletUseSeq;

public interface WalletUseSeqMapper extends BaseMapper<WalletUseSeq> {
	@Select("select * from wallet_use_seq where unique_code=#{uniqueCode} limit 1")
	WalletUseSeq selectByUniqueCode(@Param("uniqueCode") String uniqueCode);

	List<WalletUseSeq> selectLeftByUserIdTypeList(@Param("userId") Integer userId,
			@Param("typeList") List<WalletEnum.Type> typeList);

	@Update("update wallet_use_seq set left_amount = left_amount - #{reduceAmount},remark = #{remark} where id = #{id} and left_amount >= #{reduceAmount}")
	Integer decreaseLeftAmount(@Param("id") Integer id, @Param("reduceAmount") BigDecimal reduceAmount,
			@Param("remark") String remark);

	List<WalletUseSeq> selectUseByUserIdTypeList(@Param("userId") Integer userId,
			@Param("typeList") List<WalletEnum.Type> typeList);
	
//	@Select("select * from wallet_use_seq where order_code=#{orderCode} order by id desc")
//	List<WalletUseSeq> selectByOrderCode(@Param("orderCode") String orderCode);

	@Update("update wallet_use_seq set left_amount = left_amount + #{returnAmount},remark = #{remark} where id = #{id}")
	Integer returnLeftAmount(@Param("id") Integer id, @Param("returnAmount") BigDecimal returnAmount,
			@Param("remark") String remark);
}
