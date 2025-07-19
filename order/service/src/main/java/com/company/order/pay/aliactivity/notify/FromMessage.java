package com.company.order.pay.aliactivity.notify;

import java.util.Map;

public interface FromMessage {

	void handle(Integer payNotifyId, Map<String, String> aliParams);

}
