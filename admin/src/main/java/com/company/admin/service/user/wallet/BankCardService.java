package com.company.admin.service.user.wallet;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.wallet.BankCard;
import com.company.common.exception.BusinessException;
import com.company.admin.mapper.user.wallet.BankCardDao;
import com.company.admin.service.system.ConfigService;
import com.company.common.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * 银行卡Service
 * Created by xuxiaowei on 2018/11/21.
 */
@Service
public class BankCardService {

	public static final Logger logger = LoggerFactory.getLogger(BankCardService.class);

	@Autowired
	private BankCardDao bankCardDao;

	@Autowired
	private ConfigService configService;

	public void save(BankCard bankCard) {
		String verityType = configService.findByKey("verifyBankCard", "1");
		verify(bankCard, verityType);
		if (!verityType.equals("1")) {
			JsonNode bankCardInfo = requestBankCardInfo(bankCard.getMobile(), bankCard.getCardNumber(), bankCard.getOwner(),
					bankCard.getIdentityNumber());
			bankCard.setBankName(bankCardInfo.at("/result/information/bankname").asText() + "/" +
					bankCardInfo.at("/result/information/cardname").asText() + "/" +
					bankCardInfo.at("/result/information/cardtype").asText());
			bankCard.setBankInfo(bankCardInfo.toString());
		}
		bankCardDao.save(bankCard);
	}

	public void remove(BankCard bankCard) {
		BankCard existent = get(bankCard);
		bankCardDao.remove(existent);
	}

	public void update(BankCard bankCard) {
		BankCard existedCard = get(bankCard);
		existedCard.setCardNumber(bankCard.getCardNumber());
		existedCard.setOwner(bankCard.getOwner());
		existedCard.setIdentityNumber(bankCard.getIdentityNumber());
		existedCard.setMobile(bankCard.getMobile());
		existedCard.setBankName(bankCard.getBankName());
		existedCard.setBankInfo(bankCard.getBankInfo());
		
		String verityType = configService.findByKey("verifyBankCard", "1");
		verify(bankCard, verityType);
		if (!verityType.equals("1")) {
			JsonNode bankCardInfo = requestBankCardInfo(bankCard.getMobile(), bankCard.getCardNumber(), bankCard.getOwner(),
					bankCard.getIdentityNumber());
			bankCard.setBankName(bankCardInfo.at("/result/information/bankname").asText() + "/" +
					bankCardInfo.at("/result/information/cardname").asText() + "/" +
					bankCardInfo.at("/result/information/cardtype").asText());
			bankCard.setBankInfo(bankCardInfo.toString());
		}
		bankCardDao.update(existedCard);
	}

	public BankCard get(BankCard bankCard) {
		BankCard existent = bankCardDao.get(bankCard);
		if (existent == null) {
			throw new BusinessException("银行卡不存在");
		}
		return existent;
	}

	public XSPageModel<BankCard> listAndCount(BankCard bankCard) {
		return XSPageModel.build(bankCardDao.list(bankCard), bankCardDao.count(bankCard));
	}

	private void verify(BankCard bankCard, String verifyType) {
		if (verifyType.equals("2")) {
			if (StringUtils.isBlank(bankCard.getOwner())) {
				throw new BusinessException("开户人不能为空");
			}
		}
		else if (verifyType.equals("3")) {
			if (StringUtils.isBlank(bankCard.getOwner())) {
				throw new BusinessException("开户人不能为空");
			}
			if (StringUtils.isBlank(bankCard.getIdentityNumber())) {
				throw new BusinessException("身份证不能为空");
			}
		}
		else if (verifyType.equals("4")) {
			if (StringUtils.isBlank(bankCard.getOwner())) {
				throw new BusinessException("开户人不能为空");
			}
			if (StringUtils.isBlank(bankCard.getIdentityNumber())) {
				throw new BusinessException("身份证不能为空");
			}
			if (StringUtils.isBlank(bankCard.getMobile())) {
				throw new BusinessException("手机号不能为空");
			}
		}
		else {
			return;
		}
	}

	private JsonNode requestBankCardInfo(String mobile, String cardNumber, String owner, String identityNumber) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "APPCODE " + "9a36b43ce83146f8a0867aace2319c30");
		Map<String, String> params = new HashMap<>();
		params.put("Mobile", mobile);
		params.put("bankcard", cardNumber);
		params.put("cardNo", identityNumber);
		params.put("realName", owner);
//		String responseStr = xsHttpClientService.makeHttpRequestStringResult(
//				"https://aliyun-bankcard-verify.apistore.cn/bank", headers, "GET", params);
		String responseStr = "{\"error_code\": 0, \"result\": {\"information\": {\"bankname\": \"中国银行\", \"cardname\": \"金穗通宝卡(银联卡)\", \"cardtype\": \"银联借记卡\"}}}";
		JsonNode info = JsonUtil.readTree(responseStr);
		if (info.get("error_code").asInt() != 0) {
			logger.error("验证银行卡信息失败：{}", responseStr);
			throw new BusinessException(info.get("reason").asText());
		}
		return info;
	}
}
