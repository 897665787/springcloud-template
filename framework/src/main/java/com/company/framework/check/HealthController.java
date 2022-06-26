package com.company.framework.check;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;

@RestController
public class HealthController {
	@GetMapping("/health")
	public Result<String> health() {
		return Result.success("success");
	}
}
