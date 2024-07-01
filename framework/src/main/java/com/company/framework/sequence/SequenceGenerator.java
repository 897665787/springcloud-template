package com.company.framework.sequence;

public interface SequenceGenerator {
	long nextId();

	default String nextStrId() {
		return String.valueOf(nextId());
	}
}