package com.company.admin.oss.sts.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.company.admin.oss.sts.server.SecurityTokenServer;

@Aspect
public class OSSTokenAspect {
	@Autowired
	private SecurityTokenServer securityTokenSever;

	@After("@annotation(com.company.admin.oss.sts.annotation.OSSToken)")
	public void doAfter(JoinPoint joinPoint) throws Throwable {
		Model model = null;
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if ((arg instanceof Model)) {
				model = (Model) arg;
				break;
			}
		}
		if (model == null) {
			throw new Exception("没有注入Modal");
		}
		model.addAttribute("ossToken", this.securityTokenSever.getToken());
	}
}
