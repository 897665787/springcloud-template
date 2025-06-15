package com.company.web.req;

import cn.licoy.encryptbody.annotation.decrypt.AESDecryptBody;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

//@AESDecryptBody(key = "1468657f04e24e4d8c43f1b8b4032984")
@AESDecryptBody
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DecryptEntityReq {
	String name;
	String value;
}
