package com.company.web.service;

import org.springframework.cache.annotation.Cacheable;

public interface TimeService {
	@Cacheable(value = "time")
	String getTime();

	String getCacheTime();
}
