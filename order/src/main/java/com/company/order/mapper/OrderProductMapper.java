package com.company.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.order.entity.OrderProduct;

public interface OrderProductMapper extends BaseMapper<OrderProduct> {
	@Select("select * from bu_order_product where order_code=#{orderCode}")
	List<OrderProduct> selectByOrderCode(@Param("orderCode") String orderCode);

	@Select("select * from bu_order_product where order_code = #{orderCode} limit 1")
	OrderProduct selectOneByOrderCode(@Param("orderCode") String orderCode);

	List<OrderProduct> selectByOrderCodes(@Param("orderCodes") List<String> orderCodes);

	@Select("select * from bu_order_product where order_code = #{orderCode} and store_id = #{storeId}")
	List<OrderProduct> selectByOrderCodeAndStoreId(@Param("orderCode") String orderCode,
			@Param("storeId") String storeId);

	@Select("select ifnull(sum(number), 0) from `lssq-takeout`.bu_order_product where product_code = #{productCode} and create_time > #{beginTime}")
	Integer countByProductCode(@Param("productCode") String productCode, @Param("beginTime") Date beginTime);
}
