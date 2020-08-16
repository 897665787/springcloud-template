package com.company.user.service;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderReq;
import com.company.order.api.response.OrderResp;
import com.company.user.entity.City;
import com.company.user.entity.User;
import com.company.user.mapper.common.CityMapper;
import com.company.user.mapper.user.UserMapper;

@Component
public class UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CityMapper cityMapper;
	@Autowired
	private OrderFeign orderFeign;
//	@Autowired
//	private SqlSessionFactory sqlSessionFactory;

	public User getById(Long id) {
		User selectById = userMapper.selectById(1);
		System.out.println("selectById:" + selectById);
		
		City selectById2 = cityMapper.selectById(1302);
		System.out.println("selectById2:" + selectById2);
//		OrderResp orderResp = orderFeign.getById(1L);
//		System.out.println("orderResp:" + orderResp);

		
//		List<User> selectByName = userMapper.selectByName("江庆棣");
//		System.out.println("selectByName:" + selectByName);
		
		OrderReq orderReq = new OrderReq().setId(3L).setOrderCode(String.valueOf(System.currentTimeMillis()));
		OrderResp save = orderFeign.save(orderReq);
		System.out.println("save:" + save);
		return new User().setId(id).setUsername(String.valueOf(System.currentTimeMillis()));
	}

//	@Transactional
	public void save() {
//		System.out.println("sqlSessionFactory:" + sqlSessionFactory);
		
		User entity = new User().setName("ad").setUsername("basd").setPassword("186027de6ac204029da11c377024350c")
				.setAvatar("http://demo.ruoyi.vip/img/profile.jpg")
				.setStatus(0);
		int insert = userMapper.insert(entity);
		System.out.println("insert:" + insert);
		
	}
}
