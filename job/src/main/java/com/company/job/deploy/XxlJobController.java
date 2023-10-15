package com.company.job.deploy;

import java.util.Optional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.annotation.PublicUrl;
import com.company.common.api.Result;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

/**
 * 部署相关接口（用于优雅发版）
 * 
 * @author JQ棣
 *
 */
@PublicUrl
@RestController
public class XxlJobController {

	@Autowired(required = false)
	private XxlJobSpringExecutor xxlJobExecutor;

	/**
	 * 服务下线
	 * 
	 * @return
	 */
	@RequestMapping(value = "/executor/destroy", method = RequestMethod.GET)
	public Result<?> destroy() {
		try {
			// 下线执行器
			Optional.ofNullable(xxlJobExecutor).ifPresent(XxlJobSpringExecutor::destroy);
			return Result.success();
		} catch (Exception e) {
			return Result.fail(ExceptionUtils.getMessage(e));
		}
	}
}
