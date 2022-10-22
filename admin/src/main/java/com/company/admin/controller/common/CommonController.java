package com.company.admin.controller.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.company.admin.entity.security.SecStaff;
import com.company.admin.oss.OssService;
import com.company.admin.oss.sts.model.SecurityToken;
import com.company.admin.oss.sts.server.SecurityTokenServer;
import com.company.admin.service.security.SecStaffService;
import com.company.admin.util.AESUtil;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;


/**
 * Created by gustinlau on 10/30/17.
 */
@Controller
public class CommonController {

    @Autowired
    OssService ossService;
    @Autowired
    SecurityTokenServer stsServer;
    @Autowired
    SecStaffService secStaffService;
    @Value("${aes.key}")
    private String AES_KEY;

    @RequestMapping("/admin/editor/token")
    @ResponseBody
    public Result<?> ossTokenEncrypt(String token) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) authentication.getPrincipal()).getUsername()));
        if (token != null && token.equals(secStaff.getId())) {
            SecurityToken securityToken = stsServer.getToken();
            if ("success".equals(securityToken.getCode())) {
                return Result.success(AESUtil.encrypt(JsonUtil.toJsonString(securityToken), AES_KEY));
            } else {
                return Result.fail("Token获取失败 " + securityToken.getMsg());
            }
        } else {
            throw new Exception("Token校验失败");
        }
    }


    /**
     * 文件上传
     *
     * @param folders
     * @param files
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/common/api/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> fileUpload(String[] folders, MultipartFile[] files) throws Exception {
        List<String> resultList = ossService.imageUpload(folders, files);
        if (resultList == null) {
            throw new Exception();
        }
        return Result.success(resultList);
    }
}
