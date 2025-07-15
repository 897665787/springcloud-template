package com.company.order.api.response;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <pre>
{
	"response": {
		"code": "10000",
		"msg": "Success",
		"order_no": "2015042321001004720200028594",
		"buyer_id": "2088xxxx",
		"custom_send_time": "2017-01-01 00:00:01",
		"send_activity_info_result_list": {
			"activity_id": "2016042700826004508401111111",
			"send_voucher_info_result_list": [
				{
					"voucher_code": "123AB",
					"voucher_code_url": "alipays://platformapi/startapp?appId=XXX",
					"merchant_order_url": "alipays://platformapi/startapp?appId=XXX"
				}
			]
		}
	},
	"sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
}
 * </pre>
 */
@Data
@Accessors(chain = true)
public class SpiOrderSendNotifyResp {
	private Response response;
	private String sign;

	@Data
	@Accessors(chain = true)
	public static class Response {
		private String code;
		private String msg;

		private String sub_code;
		
		private String sub_msg;

		private String order_no;

		private String buyer_id;

		private String custom_send_time;

		private SendActivityInfoResultList send_activity_info_result_list;

		@Data
		@Accessors(chain = true)
		public static class SendActivityInfoResultList {
			private String activity_id;

			private List<SendVoucherInfoResult> send_voucher_info_result_list;

			@Data
			@Accessors(chain = true)
			public static class SendVoucherInfoResult {
				private String merchant_order_url;
				
				private String voucher_code;

				private String voucher_code_url;

			}
		}

	}
}
