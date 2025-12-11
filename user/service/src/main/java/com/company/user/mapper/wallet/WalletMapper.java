package com.company.user.mapper.wallet;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.api.enums.WalletEnum;
import com.company.user.entity.Wallet;

public interface WalletMapper extends BaseMapper<Wallet> {

	@Select("select * from wallet where user_id = #{userId} and type = #{type.code}")
	Wallet selectByUserIdType(@Param("userId") Integer userId, @Param("type") WalletEnum.Type type);

	@Update("update wallet set balance = balance + #{amount} where id = #{id}")
	int income(@Param("id") Integer id, @Param("amount") BigDecimal amount);

	@Update("update wallet set balance = balance - #{amount} where id = #{id} and balance >= #{amount}")
	int outcome(@Param("id") Integer id, @Param("amount") BigDecimal amount);
}
