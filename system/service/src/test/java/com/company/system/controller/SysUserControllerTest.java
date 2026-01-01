package com.company.system.controller;

import cn.hutool.json.JSONUtil;
import com.company.system.api.response.SysUserInfoResp;
import org.junit.jupiter.api.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SysUserControllerTest {

	@Autowired
	private SysUserController sysUserController;

	@Test
	void getInfo() {
		SysUserInfoResp resp = sysUserController.getInfo(1);
		System.out.println(JSONUtil.toJsonPrettyStr(resp));
	}
}
