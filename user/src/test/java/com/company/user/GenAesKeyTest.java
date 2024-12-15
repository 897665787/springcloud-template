package com.company.user;

import java.util.List;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import io.github.encrypt.handlers.DefaultEncryptor;
import io.github.encrypt.handlers.IEncryptor;

public class GenAesKeyTest {

	@Test
	public void genKey() {
		String key = RandomUtil.randomString(16);// 16 bits
		System.out.println(key);
		AES aes = SecureUtil.aes(key.getBytes());
		SecretKey secretKey = aes.getSecretKey();
		System.out.println(secretKey.toString());
		System.out.println(aes.encryptHex("1468657f04e24e4d8c43f1b8b4032984"));
		System.out.println(aes.encryptHex("18888888888"));
		System.out.println(aes.encryptHex("candi@yeahka.com"));
	}

	@Test
	public void testKey() {
		String key = "ublp45r318fr4xr7";// 16 bits
		IEncryptor encryptor = new DefaultEncryptor(key);

		List<String> contentList = Lists.newArrayList("18888888888", "candi@yeahka.com",
				"1468657f04e24e4d8c43f1b8b4032984", "张名字", "6238558521445575");
		for (String content : contentList) {
			System.out.println("content:" + content);
			String encrypt = encryptor.encrypt(content);
			System.out.println("encrypt:" + encrypt);
			String decrypt = encryptor.decrypt(encrypt);
			System.out.println("decrypt:" + decrypt);
		}

		// mysql加解密
//		SELECT HEX(AES_ENCRYPT('13322986137', 'as1NeqNbp5h5TIk5')) AS encrypted_data;
//		SELECT AES_DECRYPT(UNHEX('14088A0FC031A6948DAAC35020DAC9EE'), 'as1NeqNbp5h5TIk5') AS decrypted_data;
	}
}
