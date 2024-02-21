package com.company.order.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.company.order.api.enums.OrderEnum;
import com.company.order.entity.Order;

public interface OrderMapper extends BaseMapper<Order> {

	List<Order> selectByUserIdAndStatus(Page<Order> page, @Param("userId") Integer userId,
			@Param("statusEnum") OrderEnum.StatusEnum statusEnum);

	@Select("select * from bu_order where order_code = #{orderCode}")
	Order selectByOrderCode(@Param("orderCode") String orderCode);

	@Select("select id from bu_user_order where business_id = #{orderCode} and order_type = #{businessTypeEnum.type}")
	Integer selectIdByBusinessTypeAndOrderCode(@Param("businessTypeEnum") OrderEnum.OrderType businessTypeEnum,
			@Param("orderCode") Integer orderCode);

	@Insert("INSERT INTO bu_order(user_id, order_code, order_type, status, sub_status, product_amount, order_amount, reduce_amount, need_pay_amount,sub_order_url)"
			+ " VALUES (#{userId}, #{orderCode}, #{orderType}, #{status}, #{subStatus}, #{productAmount}, #{orderAmount}, #{reduceAmount}, #{needPayAmount}, #{subOrderUrl})"
			+ " ON DUPLICATE KEY UPDATE"
			+ " status = #{status},sub_status = #{subStatus},product_amount = #{productAmount},order_amount = #{orderAmount},reduce_amount = #{reduceAmount},need_pay_amount = #{needPayAmount},sub_order_url = #{subOrderUrl}")
	Integer saveOrUpdate(Order order);

	@Select("select * from bu_order where user_id = #{userId} and sub_status in(30,31,32,33,41) order by id desc")
	List<Order> selectUnFinishOrder(@Param("userId") Integer userId);

	@Select("select order_code from bu_order where `sub_status` = #{subStatus} limit #{offset}, #{pageSize}")
	List<String> listWithSubStatus(@Param("subStatus") Integer subStatus, @Param("offset") int offset,
			@Param("pageSize") Integer pageSize);

	List<Order> selectCompleteForComment();

	Order selectIsNewPerson(@Param("userId") Integer userId, @Param("businessType") String businessType);

	List<Order> selectByOrderCodesAndStatus(@Param("orderCodeSet") Set<String> orderCodeSet,
			@Param("statusSet") Set<Integer> statusSet);

	/**
	 * 查询用户外卖首单（非取消和退款的订单）
	 */
	Order selectFirstOrder(@Param("userId") Integer userId);

	@Select("select * from bu_order where user_id = #{userId} and order_type = 'groupmeal' and sub_status in(30,31,32,33,41) order by id desc")
	List<Order> selectUnfinishedGroupMealOrder(@Param("userId") Integer userId);

	/**
	 * 获取最新的已完成订单
	 */
	Order selectNewestMealSuccessOrder4GroupMeal(@Param("userId") Integer userId);

	@Select("select create_time from bu_order where user_id = #{userId} and status in(1,3,4,5) order by id desc")
	Date lastOrderTime(@Param("userId") Integer userId);
}
