package com.company.user.mapper.wallet;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.user.api.enums.WalletRecordEnum;
import com.company.user.entity.WalletRecord;

public interface WalletRecordMapper extends BaseMapper<WalletRecord> {

	@Select("select * from bu_wallet_record where wallet_id=#{walletId} order by id desc")
	List<WalletRecord> pageByWalletId(Page<WalletRecord> page, @Param("walletId") Integer walletId);

	@Select("select * from bu_wallet_record where unique_code=#{uniqueCode} and type = #{type.code} limit 1")
	WalletRecord selectByUniqueCodeType(@Param("uniqueCode") String uniqueCode,@Param("type") WalletRecordEnum.Type type);
}
