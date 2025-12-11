package com.company.user.mapper.wallet;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.entity.WalletPreincome;

public interface WalletPreincomeMapper extends BaseMapper<WalletPreincome> {

	@Select("select sum(amount) from wallet_preincome where wallet_id = #{walletId} and status = 1")
	BigDecimal sumAmount(@Param("walletId") Integer walletId);

	@Select("select id from wallet_preincome where status = 1 limit #{limit}")
	List<Integer> selectId4Income(@Param("limit") Integer limit);

	@Update("update wallet_preincome set status = 2 where id = #{id} and status = 1")
	Integer update4Income(@Param("id") Integer id);

	@Select("select * from wallet_preincome where unique_code = #{uniqueCode} limit 1")
	WalletPreincome selectByUniqueCode(@Param("uniqueCode") String uniqueCode);
}
