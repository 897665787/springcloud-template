package com.company.framework.lock;

import java.util.function.Supplier;

public interface LockClient {
	<V> V doInLock(String name, Supplier<V> supplier);
}
