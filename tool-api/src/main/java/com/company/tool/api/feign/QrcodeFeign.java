package com.company.tool.api.feign;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.request.WxaCodeReq;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/qrcode", fallbackFactory = QrcodeFeign.QrcodeFeignFactory.class)
public interface QrcodeFeign {

	/**
	 * 获取小程序码并上传至图片服务
	 * 
	 * @param wxaCodeReq
	 * @return 小程序码图片链接
	 */
	@PostMapping(value = "/wxaCode2upload")
	String wxaCode2upload(@RequestBody WxaCodeReq wxaCodeReq);

	/**
	 * 获取小程序码（建议优先使用wxaCode2upload，除非上传图片到服务器有性能问题）
	 * 
	 * @param wxaCodeReq
	 * @return 小程序码byte数组
	 */
	@PostMapping(value = "/wxaCode")
	byte[] wxaCode(@RequestBody WxaCodeReq wxaCodeReq);

	@Component
	class QrcodeFeignFactory implements FallbackFactory<QrcodeFeign> {

		@Override
		public QrcodeFeign create(Throwable throwable) {
			return new QrcodeFeign() {

				@Override
				public String wxaCode2upload(WxaCodeReq wxaCodeReq) {
					return Result.onFallbackError();
				}

				@Override
				public byte[] wxaCode(WxaCodeReq wxaCodeReq) {
					return Result.onFallbackError();
				}

			};
		}
	}

}
