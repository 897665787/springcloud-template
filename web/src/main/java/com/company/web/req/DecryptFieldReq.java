package com.company.web.req;

import cn.licoy.encryptbody.annotation.FieldBody;
import cn.licoy.encryptbody.annotation.decrypt.AESDecryptBody;
import cn.licoy.encryptbody.annotation.decrypt.DecryptBody;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@DecryptBody
@FieldBody
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DecryptFieldReq {
	Integer id;

//	@AESDecryptBody(key = "1468657f04e24e4d8c43f1b8b4032984")
	@AESDecryptBody
	@FieldBody(field = "name")
	String name;
}
