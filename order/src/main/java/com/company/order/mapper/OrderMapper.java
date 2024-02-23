package com.company.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.company.order.api.enums.OrderEnum;
import com.company.order.entity.Order;

public interface OrderMapper extends BaseMapper<Order> {

	List<Order> pageByUserIdAndStatus(Page<Order> page, @Param("userId") Integer userId,
			@Param("statusEnum") OrderEnum.StatusEnum statusEnum);

	@Select("select * from bu_order where order_code = #{orderCode}")
	Order selectByOrderCode(@Param("orderCode") String orderCode);

	@Insert("INSERT INTO bu_order(user_id, order_code, order_type, status, sub_status, product_amount, order_amount, reduce_amount, need_pay_amount, sub_order_url, attach)"
			+ " VALUES (#{userId}, #{orderCode}, #{orderType}, #{status}, #{subStatus}, #{productAmount}, #{orderAmount}, #{reduceAmount}, #{needPayAmount}, #{subOrderUrl}, #{attach})"
			+ " ON DUPLICATE KEY UPDATE"
			+ " status = #{status},sub_status = #{subStatus},product_amount = #{productAmount},order_amount = #{orderAmount},reduce_amount = #{reduceAmount},need_pay_amount = #{needPayAmount},sub_order_url = #{subOrderUrl},attach = #{attach}")
	Integer saveOrUpdate(Order order);
}
