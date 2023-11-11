package com.company.order.api.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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

		@JsonProperty("sub_code")
		private String subCode;
		
		@JsonProperty("sub_msg")
		private String subMsg;

		@JsonProperty("order_no")
		private String orderNo;

		@JsonProperty("buyer_id")
		private String buyerId;

		@JsonProperty("custom_send_time")
		private String customSendTime;

		@JsonProperty("send_activity_info_result_list")
		private SendActivityInfoResultList sendActivityInfoResultList;

		@Data
		@Accessors(chain = true)
		public static class SendActivityInfoResultList {
			@JsonProperty("activity_id")
			private String activityId;

			@JsonProperty("send_voucher_info_result_list")
			private List<SendVoucherInfoResult> sendVoucherInfoResultList;

			@Data
			@Accessors(chain = true)
			public static class SendVoucherInfoResult {
				@JsonProperty("merchant_order_url")
				private String merchantOrderUrl;
				
				@JsonProperty("voucher_code")
				private String voucherCode;

				@JsonProperty("voucher_code_url")
				private String voucherCodeUrl;

			}
		}

	}
}
