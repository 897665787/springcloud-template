package com.company.web.resp;

import cn.licoy.encryptbody.annotation.FieldBody;
import cn.licoy.encryptbody.annotation.encrypt.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EncryptBody
@FieldBody
public class EncryptFieldResp {
    Integer id;
    String name;

    @FieldBody
    @AESEncryptBody
    String aesencryptbody;

    @FieldBody
    @DESEncryptBody
    String desencryptbody;

    @FieldBody
    @MD5EncryptBody
    String md5encryptbody;

//    @FieldBody
//    @RSAEncryptBody
//    String rsaencryptbody;
//
//    @FieldBody
//    @SHAEncryptBody
//    String shaencryptbody;
}
