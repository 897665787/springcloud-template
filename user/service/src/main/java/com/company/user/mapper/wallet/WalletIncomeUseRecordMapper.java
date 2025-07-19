package com.company.user.mapper.wallet;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.api.enums.WalletRecordEnum;
import com.company.user.entity.WalletIncomeUseRecord;

public interface WalletIncomeUseRecordMapper extends BaseMapper<WalletIncomeUseRecord> {

	@Select("select * from bu_wallet_income_use_record where wallet_id = #{walletId} and status in(1,2) order by invalid_time asc,id asc")
	List<WalletIncomeUseRecord> select2Use(@Param("walletId") Integer walletId);

	@Update("update bu_wallet_income_use_record set unused_amount = unused_amount - #{usedAmount},status = #{status.code},remark = #{remark} where id = #{id} and status in(1,2) and unused_amount >= #{usedAmount}")
	Integer update2use(@Param("usedAmount") BigDecimal usedAmount,
			@Param("status") WalletRecordEnum.IncomeRecordUseStatus status, @Param("remark") String remark,
			@Param("id") Integer id);

	@Select("select id from bu_wallet_income_use_record where invalid_time < now() and status in(1,2) limit #{limit}")
	List<Integer> selectId4Expire(@Param("limit") Integer limit);

	@Update("update bu_wallet_income_use_record set status = #{status.code} where id = #{id} and status in(1,2) and invalid_time < now()")
	Integer update4Expire(@Param("status") WalletRecordEnum.IncomeRecordUseStatus status, @Param("id") Integer id);

	@Select("select * from bu_wallet_income_use_record where wallet_record_id = #{walletRecordId} limit 1")
	WalletIncomeUseRecord selectByWalletRecordId(@Param("walletRecordId") Integer walletRecordId);

}
