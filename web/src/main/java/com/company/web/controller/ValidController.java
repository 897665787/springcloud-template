package com.company.web.controller;

import com.company.common.api.Result;
import com.company.web.req.Param;
import com.company.web.req.ValidNestingReq;
import com.company.web.req.ValidReq;
import com.google.common.collect.Maps;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/valid")
public class ValidController {

    // get
    @GetMapping(value = "/get-params")
    public Result<?> getparams(@NotBlank(message = "p不能为空") String p) {
        // 需结合@Validated使用
        return Result.success(p);
    }

    @GetMapping(value = "/get-body-form-data")
    public Result<?> getbodyformdata(@NotBlank String p1, @NotEmpty String p2, @NotNull Integer p3) {
        // 需结合@Validated使用
        Map<String, Object> map = Maps.newHashMap();
        map.put("p1", p1);
        map.put("p2", p2);
        map.put("p3", p3);
        return Result.success(map);
    }

	@GetMapping(value = "/get-body-form-data2")
    public Result<?> getbodyformdata2(@Valid ValidReq req) {
        // @Valid无需结合@Validated使用
        return Result.success(req);
    }

    // post
    @PostMapping(value = "/post-params")
    public Result<?> postparams(@NotBlank(message = "p不能为空") String p) {
        // 需结合@Validated使用
        return Result.success(p);
    }

	@PostMapping(value = "/post-body-form-data")
	public Result<?> postbodyformdata(@NotBlank String p1, @NotEmpty String p2, @NotNull Integer p3) {
        // 需结合@Validated使用
		Map<String, Object> map = Maps.newHashMap();
		map.put("p1", p1);
		map.put("p2", p2);
		map.put("p3", p3);
		return Result.success(map);
	}

	@PostMapping(value = "/post-body-form-data2")
	public Result<?> postbodyformdata2(@Valid ValidReq req) {
        // @Valid无需结合@Validated使用
		return Result.success(req);
	}

	@PostMapping(value = "/post-body-row2")
	public Result<?> postbodyrow2(@NotBlank(message = "p不能为空") @RequestBody String param) {
		// POST /reqlog/post-body-row {"asdasd":1,"sadsaddd":" asdas dasd"} 5
		HashMap<Object, Object> newHashMap = Maps.newHashMap();
		newHashMap.put("param", param);
		return Result.success(newHashMap);
	}

    @PostMapping(value = "/post-body-row3")
    public Result<?> postbodyrow3(@Valid @RequestBody ValidNestingReq req) {
        // @Valid无需结合@Validated使用
        return Result.success(req);
    }
}
