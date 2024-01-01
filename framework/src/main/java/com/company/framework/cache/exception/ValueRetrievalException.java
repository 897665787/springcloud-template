package com.company.framework.cache.exception;

public class ValueRetrievalException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ValueRetrievalException(Exception e) {
		super(e);
	}
}
