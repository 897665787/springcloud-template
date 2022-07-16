package com.company.auth.wx.mp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.auth.wx.mp.config.WxMpConfiguration;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * jsapi 演示接口的 controller.
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 * @date 2020-04-25
 */
@AllArgsConstructor
@RestController
@RequestMapping("/wx/jsapi/{appid}")
public class WxJsapiController {

    @GetMapping("/getJsapiTicket")
    public String getJsapiTicket(@PathVariable String appid) throws WxErrorException {
    	WxMpService wxService = WxMpConfiguration.getMpService(appid);
        final WxJsapiSignature jsapiSignature = wxService.createJsapiSignature("111");
        System.out.println(jsapiSignature);
        return wxService.getJsapiTicket(true);
    }
}
