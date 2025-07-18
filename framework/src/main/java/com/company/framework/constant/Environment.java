package com.company.framework.constant;

import java.util.List;

import com.google.common.collect.Lists;

public interface Environment {
	String LOCAL = "local";
	String DEV = "dev";
	String TEST = "test";
	String PRE = "pre";
	String PROD = "prod";

	List<String> TEST_ENVIRONMENT = Lists.newArrayList(Environment.LOCAL, Environment.DEV, Environment.TEST,
			Environment.PRE);
	
	List<String> PRODUCE_ENVIRONMENT = Lists.newArrayList(Environment.PROD);
}
