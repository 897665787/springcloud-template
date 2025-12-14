package com.company.web.service.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.company.framework.constant.Environment;
import com.company.web.service.TimeService;

@Service("mockSuningTimeService")
@Primary
@Profile({ Environment.LOCAL, Environment.DEV, Environment.TEST, Environment.PRE })
@ConditionalOnProperty(prefix = "template.mock", name = "time", havingValue = "true")
public class SuningTimeService implements TimeService {

	@Override
	public String getTime() {
		return "mock data";
	}

	@Override
	public String getCacheTime() {
		return "mock data";
	}
}
