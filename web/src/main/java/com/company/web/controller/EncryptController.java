package com.company.web.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.annotation.EncryptResultData;
import com.company.common.annotation.NoResultWrapper;
import com.company.common.api.Result;
import com.company.common.exception.BusinessException;

@RestController
@RequestMapping("/encrypt")
public class EncryptController {

	@PostMapping(value = "/post-body-row")
	@EncryptResultData
	public Map<String, Object> postbodyrow(@RequestBody Map<String, Object> param) {
		if(true){
			throw new BusinessException("asdasd");
		}
		return param;
	}

	@PostMapping(value = "/post-body-row-result")
	@EncryptResultData
	public Result postbodyrowresult(@RequestBody Map<String, Object> param) {
		return Result.success(param);
	}

	@PostMapping(value = "/post-body-row2")
	@EncryptResultData
//	@NoResultWrapper
	public String postbodyrownoresult2(@RequestBody Map<String, Object> param) {
		return "SUCCESS";
	}
}
