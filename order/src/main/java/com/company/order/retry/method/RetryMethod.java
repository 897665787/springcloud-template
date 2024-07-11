package com.company.order.retry.method;

public interface RetryMethod<M> {
	RetryResult invoke(M method, Object params, int failure, int maxFailure);
}
