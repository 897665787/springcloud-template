package com.company.tool.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.common.exception.BusinessException;
import com.company.framework.util.ClasspathUtil;
import com.company.tool.api.enums.PopupEnum;
import com.company.tool.api.enums.SmsEnum;
import com.company.tool.api.enums.WebhookEnum;
import com.company.tool.api.feign.FileFeign;
import com.company.tool.api.feign.PopupFeign;
import com.company.tool.api.request.CancelUserPopupReq;
import com.company.tool.api.request.ClientUploadReq;
import com.company.tool.api.request.CreateUserPopupReq;
import com.company.tool.api.response.ClientUploadResp;
import com.company.tool.sms.AsyncSmsSender;
import com.company.tool.webhook.AsyncWebhookSender;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/test")
public class TestController {

	@Autowired
	private FileFeign fileFeign;
	@Autowired
	private VerifyCodeController verifyCodeFeign;
	@Autowired
	private AsyncSmsSender asyncSmsSender;
	@Autowired
	private AsyncWebhookSender asyncWebhookSender;
	@Autowired
	private PopupFeign popupFeign;

	@GetMapping("/verifyCodeSms")
	public String verifyCodeSms(String mobile, String type) {
		return verifyCodeFeign.sms(mobile, type);
	}

	@GetMapping("/webhook")
	public String webhook() {
		Map<String, String> templateParamMap = Maps.newHashMap();
		templateParamMap.put("time", DateUtil.now());
		templateParamMap.put("orderCode", "d222222222222");
		asyncWebhookSender.send(WebhookEnum.Type.SYSTEM_ERROR, templateParamMap);
		return null;
	}

	@GetMapping("/batchSendSms")
	public List<Integer> testBatchSendSms() {
		List<String> mobileList = Lists.newArrayList();
		mobileList.add("152xxxxxxxx");
		mobileList.add("153xxxxxxxx");
		mobileList.add("154xxxxxxxx");

		asyncSmsSender.send0(mobileList, SmsEnum.Type.MARKET);

		return null;
	}

	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file) {
		String name = file.getName();
		String originalFilename = file.getOriginalFilename();
		String contentType = file.getContentType();
		long size = file.getSize();
		log.info("name:{},originalFilename:{},contentType:{},size:{}", name, originalFilename, contentType, size);

		if (size == 0) {
			throw new BusinessException("请选择文件");
		}

		ClientUploadReq clientUploadReq = new ClientUploadReq();
		clientUploadReq.setBasePath("web");
		clientUploadReq.setFileName(originalFilename);
		ClientUploadResp clientUploadResp = fileFeign.clientUpload(clientUploadReq);
		String fileKey = clientUploadResp.getFileKey();
		String presignedUrl = clientUploadResp.getPresignedUrl();

		try (InputStream inputStream = file.getInputStream()) {
			// 客户端使用presignedUrl上传文件
			String result = HttpRequest.put(presignedUrl).body(IOUtils.toByteArray(inputStream)).execute().body();
			log.info("result:{}", result);
			return fileKey;
		} catch (IOException e) {
			log.error("IOException", e);
			throw new BusinessException("文件上传失败");
		}
	}

	/**
	 * 完成某个操作后，埋1个用户弹窗
	 *
	 * @return
	 */
	@GetMapping("/createUserPopup")
	public ? createUserPopup() {
		PopupEnum.Model model = PopupEnum.Model.simple;
		String title = "订单配送完成";
		String text = "订单配送完成，马上去收货吧！";
		Integer priority = 1;
		Integer userId = 1;

		LocalDateTime beginTime = LocalDateTime.now();
		LocalDateTime endTime = beginTime.plusDays(7);

		CreateUserPopupReq.PopImage bgImg = new CreateUserPopupReq.PopImage();
		bgImg.setModel(model);
		bgImg.setImgUrl("https://image.com/aaa/bbb.jpg");
		bgImg.setType(PopupEnum.Type.redirect_http);
		bgImg.setValue("https://www.baidu.com");

		CreateUserPopupReq.PopButton closeBtn = new CreateUserPopupReq.PopButton();
		closeBtn.setType(PopupEnum.Type.close);
		closeBtn.setText("X");
		closeBtn.setValue("");

		CreateUserPopupReq createUserPopupReq = new CreateUserPopupReq();
		createUserPopupReq.setUserId(userId);
		createUserPopupReq.setBeginTime(beginTime);
		createUserPopupReq.setEndTime(endTime);
		createUserPopupReq.setPriority(priority);
		createUserPopupReq.setTitle(title);
		createUserPopupReq.setText(text);
		createUserPopupReq.setBgImg(bgImg);
		createUserPopupReq.setCloseBtn(closeBtn);

		popupFeign.createUserPopup(createUserPopupReq);

		CancelUserPopupReq cancelUserPopupReq = new CancelUserPopupReq();
		cancelUserPopupReq.setUserId(userId);
		cancelUserPopupReq.setTitle("商家已接单");
		cancelUserPopupReq.setRemark("订单完成，取消商家已接单弹窗");

		popupFeign.cancelUserPopup(cancelUserPopupReq);

		return null;
	}

	public static void main(String[] args) {
//		String content = ClassPathUtil.readFileAsString("config-file/application-apollo-dev.yml");
		String content = ClasspathUtil.readFileAsString("config-file/privateKey.txt");
		System.out.println(content);
	}
}
