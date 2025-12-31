package com.company.admin.service.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.company.framework.globalresponse.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.User;
import com.company.admin.entity.user.UserAddress;
import com.company.admin.mapper.user.UserDao;
import com.company.admin.util.DescriptionUtils;
import com.company.admin.util.ExcelUtil;
import com.company.admin.util.InviteCodeUtil;
import com.company.admin.util.XSMd5Util;
import com.company.admin.util.XSUuidUtil;
import com.company.framework.context.HttpContextUtil;

/**
 * 用户ServiceImpl
 * Created by JQ棣 on 2018/05/28.
 */
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserDao userDao;

	public void save(User user) {
		User existent = userDao.getByMobile(user.getMobile());
		if (existent != null) {
			ExceptionUtil.throwException("手机号已被注册");
		}
		user.setId(XSUuidUtil.generate());
		user.setType(0);
		user.setOperationType(1);
		user.setOperationPlatform(1);
		user.setOperationIp(HttpContextUtil.requestip());
		user.setCustomerId(XSUuidUtil.generate());
		user.setInviteCode(InviteCodeUtil.generateCode());
		userDao.save(user);
	}

	public void remove(User user) {
		User existedUser = get(user);
		userDao.remove(existedUser);
	}

	public User get(User user) {
		User existedUser = userDao.get(user);
		if (existedUser == null) {
			ExceptionUtil.throwException("用户不存在");
		}
		existedUser.setPassword(null);
		return existedUser;
	}

	public void update(User user) {
		User existedUser = userDao.get(user);
		if (existedUser == null) {
			ExceptionUtil.throwException("用户不存在");
		}
		if (StringUtils.isNotBlank(user.getPassword())) {
			user.setPassword(XSMd5Util.encode(user.getPassword()));
		}

		userDao.update(user);
	}

	public XSPageModel<User> listAndCount(User user) {
		user.setDefaultSort("create_time", "DESC");
		return XSPageModel.build(userDao.list(user), userDao.count(user));
	}

	public XSSFWorkbook export(User user) {
		//表头
		List<String> head = Arrays.asList("用户号","用户昵称","手机号","性别","出生年份","状态","注册时间");
		//内容
		List<User> list = userDao.list(user);
		List<List<String>> data = new ArrayList<>();
		for(User item: list){
			List<String> items = new ArrayList<>();
			items.add(item.getCustomerId());
			items.add(item.getNickname());
			items.add(item.getMobile());
			items.add(DescriptionUtils.description(User.class, "sex", item.getSex()));
			items.add(item.getBirthday());
			items.add(DescriptionUtils.description(User.class, "status", item.getStatus()));
			items.add(DateFormatUtils.format(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			data.add(items);
		}
		return ExcelUtil.generateExport(head, data);
	}

    /**
     * 用户地址列表
     * @param userAddress {userId}
     * @return
     */
    public List<UserAddress> userAddressList(UserAddress userAddress) {
        return userDao.userAddressList(userAddress);
    }

    public XSPageModel<?> listInvitedUser(User user) {
    	return XSPageModel.build(userDao.listInvitedUser(user), userDao.countInvitedUser(user));
	}
}
