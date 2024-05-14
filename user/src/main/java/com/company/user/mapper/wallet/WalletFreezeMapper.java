package com.company.user.mapper.wallet;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.entity.WalletFreeze;

public interface WalletFreezeMapper extends BaseMapper<WalletFreeze> {

	@Select("select * from bu_wallet_freeze where unique_code = #{uniqueCode} limit 1")
	WalletFreeze selectByUniqueCode(@Param("uniqueCode") String uniqueCode);

	@Update("update bu_wallet_freeze set status = 2 where unique_code = #{uniqueCode} and status = 1")
	Integer update2Use(@Param("uniqueCode") String uniqueCode);
	
	@Update("update bu_wallet_freeze set status = 3 where order_code = #{orderCode} and status = 1")
	Integer update2Return(@Param("orderCode") String orderCode);
	
	@Select("select sum(freeze_amount) from bu_wallet_freeze where wallet_id = #{walletId} and status = 1")
	BigDecimal sumFreezeAmount(@Param("walletId") Integer walletId);

	@Select("select * from bu_wallet_freeze where order_code = #{orderCode}")
	List<WalletFreeze> selectByOrderCode(@Param("orderCode") String orderCode);
}
