package com.company.admin.controller.user.wallet;



import com.company.common.api.Result;
import com.company.admin.entity.user.wallet.AliAccount;
import com.company.admin.service.user.wallet.AliAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 支付宝Controller
 * Created by JQ棣 on 2018/11/21.
 */
@Controller
public class AliAccountController {

	@Autowired
	private AliAccountService aliAccountService;

	@RequestMapping(value = "/admin/user/aliAccount/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> get(AliAccount aliAccount) {
		return Result.success(aliAccountService.get(aliAccount));
	}

	@RequestMapping(value = "/admin/user/aliAccount/save", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> save(@Validated(AliAccount.Save.class) AliAccount aliAccount) {
		aliAccountService.save(aliAccount);
		return Result.success();
	}

	@RequestMapping(value = "/admin/user/aliAccount/remove", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> remove(AliAccount aliAccount) {
		aliAccountService.remove(aliAccount);
		return Result.success();
	}

	@RequestMapping(value = "/admin/user/aliAccount/update", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> update(@Validated(AliAccount.Update.class) AliAccount aliAccount) {
		aliAccountService.update(aliAccount);
		return Result.success();
	}
}
