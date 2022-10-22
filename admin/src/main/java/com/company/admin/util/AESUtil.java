package com.company.admin.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    /**
     * 加密方法
     *
     * @param data 要加密的数据
     * @param key  加密key 16位
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            byte[] encrypted = cipher.doFinal(data.getBytes());

            return byte2HexStr(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b
     * @return
     */
    private static String byte2HexStr(byte[] b) {
        String tempStr;
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            tempStr = Integer.toHexString(b[n] & 0xFF);
            sb.append((tempStr.length() == 1) ? "0" + tempStr : tempStr);
        }
        return sb.toString().toUpperCase().trim();
    }

}
