package com.company.admin.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.common.api.Result;
import com.company.admin.entity.system.Province;
import com.company.admin.service.system.ProvinceService;

/**
 * 省份Controller
 * Created by JQ棣 on 2017/11/13.
 */
@Controller
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @RequestMapping(value = "/admin/system/province", method = RequestMethod.GET)
    public String index(Model model, Province province) {
        if(province.getPage() == null) province.setPage(1L);
        model.addAttribute("search", province);
        model.addAttribute("pageModel", provinceService.listAndCount(province));
        return "system/province";
    }

    @RequestMapping(value = "/admin/system/province/get", method = RequestMethod.POST)
    @ResponseBody
    public ? adminGet(Province province) {
        return provinceService.get(province);
    }

    @RequestMapping(value = "/admin/system/province/save", method = RequestMethod.POST)
    @ResponseBody
    public ? adminSave(@Validated(Province.Save.class) Province province) {
        provinceService.save(province);
        return null;
    }

    @RequestMapping(value = "/admin/system/province/remove", method = RequestMethod.POST)
    @ResponseBody
    public ? adminRemove(Province province) {
        provinceService.remove(province);
        return null;
    }

    @RequestMapping(value = "/admin/system/province/update", method = RequestMethod.POST)
    @ResponseBody
    public ? adminUpdate(@Validated(Province.Update.class) Province province) {
        provinceService.update(province);
        return null;
    }

    @RequestMapping(value = "/admin/system/province/status/update", method = RequestMethod.POST)
    @ResponseBody
    public ? adminUpdateStatus(Province province) {
        provinceService.updateStatus(province);
        return null;
    }

    @RequestMapping(value = "/admin/system/province/city/status/update", method = RequestMethod.POST)
    @ResponseBody
    public ? adminUpdateCityStatus(Province province) {
        provinceService.updateCityStatus(province);
        return null;
    }

	@RequestMapping(value = "/admin/system/province/list", method = RequestMethod.POST)
	@ResponseBody
	public ? adminProvinceList(Province province) {
		return provinceService.listCombo(province);
	}


    /**
     * 获取所有省市区列表
     */
    @RequestMapping(value = "/admin/system/region/tree", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ? tree() {
        return provinceService.tree();
    }
}
