package com.company.admin.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

import com.company.admin.annotation.Pagination;
import com.company.admin.entity.base.BaseModel;
import com.company.admin.exception.ExceptionConsts;

/**
 * 分页切面 
 * Created by JQ棣 on 2018/06/14.
 */
@Service
@Aspect
public class PaginationAspect {

	@Before("@annotation(pagination)")
	public void doBefore(JoinPoint joinPoint, Pagination pagination) throws Throwable {
		Object[] args = joinPoint.getArgs();
		int i = 0;
		for (; i < args.length; i++) {
			if (args[i] instanceof BaseModel) {
				BaseModel baseModel = (BaseModel) args[i];
				if (pagination.required()) {
					if (baseModel.getOffset() == null || baseModel.getLimit() == null) {
						throw ExceptionConsts.PAGINATION_ERROR;
					}
				} else {
					if (baseModel.getPage() == null
							&& (baseModel.getOffset() == null || baseModel.getLimit() == null)) {
						baseModel.setPage(1L);
					}
				}

				// limit 不能大于 pagination.maxLimit
				if (baseModel.getLimit() > pagination.maxLimit()) {
					baseModel.setLimit(pagination.maxLimit());
				}
				return;
			}
		}
		throw ExceptionConsts.PAGINATION_ERROR;
	}
}
