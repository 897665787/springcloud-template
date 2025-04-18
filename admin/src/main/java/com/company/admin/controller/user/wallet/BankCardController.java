package com.company.admin.controller.user.wallet;



import com.company.common.api.Result;
import com.company.admin.entity.user.wallet.BankCard;
import com.company.admin.service.user.wallet.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 银行卡Controller
 * Created by JQ棣 on 2018/11/21.
 */
@Controller
public class BankCardController {

	@Autowired
	private BankCardService bankCardService;

	@RequestMapping(value = "/admin/user/bankCard/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> get(BankCard bankCard) {
		return Result.success(bankCardService.get(bankCard));
	}

	@RequestMapping(value = "/admin/user/bankCard/save", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> save(@Validated(BankCard.Save.class) BankCard bankCard) {
		bankCardService.save(bankCard);
		return Result.success();
	}

	@RequestMapping(value = "/admin/user/bankCard/remove", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> remove(BankCard bankCard) {
		bankCardService.remove(bankCard);
		return Result.success();
	}

	@RequestMapping(value = "/admin/user/bankCard/update", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> update(@Validated(BankCard.Update.class) BankCard bankCard) {
		bankCardService.update(bankCard);
		return Result.success();
	}
}
