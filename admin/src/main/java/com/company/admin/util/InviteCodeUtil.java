package com.company.admin.util;

import java.util.Random;

/**
 * 邀请码生成器，算法原理：<br/>
 * 1) 获取id: 1127738 <br/>
 * 2) 使用自定义进制转为：gpm6 <br/>
 * 3) 转为字符串，并在后面加'o'字符：gpm6o <br/>
 * 4）在后面随机产生若干个随机数字字符：gpm6o7 <br/>
 * 转为自定义进制后就不会出现o这个字符，然后在后面加个'o'，这样就能确定唯一性。最后在后面产生一些随机字符进行补全。<br/>
 *
 * @author kunye
 */
public class InviteCodeUtil {

    /**
     * 自定义进制（选择你想要的进制数，不能重复且最好不要0、1、o、i这些容易混淆的字符）
     */
    private static final String r = "QWE8S2DZX9C7P5K3LMJUFR4VYTN6BGH";

    /**
     * 定义一个字符用来补全邀请码长度（该字符前面是计算出来的邀请码，后面是用来补全用的）
     */
    private static final char b = 'A';

    /**
     * 进制长度
     */
    private static final int binLen = r.length();

    /**
     * 邀请码长度
     */
    private static final int s = 6;

    /**
     * 生成随机码
     *
     * @return 随机码
     */
    public static String generateCode() {
        // 当前时间的Unix时间戳减去2010-01-01 00:00:00 GMT+0800的Unix时间戳；
        Long id = System.currentTimeMillis()/1000-1293840000L;

        char[] buf = new char[32];
        int charPos = 32;

        while ((id / binLen) > 0) {
            int ind = (int) (id % binLen);
            buf[--charPos] = r.charAt(ind);
            id /= binLen;
        }
        buf[--charPos] = r.charAt((int) (id % binLen));
        String str = new String(buf, charPos, (32 - charPos));
        // 不够长度的自动随机补全
        if (str.length() < s) {
            StringBuilder sb = new StringBuilder();
            sb.append(b);
            Random rnd = new Random();
            for (int i = 1; i < s - str.length(); i++) {
                sb.append(r.charAt(rnd.nextInt(binLen)));
            }
            str += sb.toString();
        }
        return str;
    }

    /**
     * 根据随机码生成ID
     *
     * @return ID
     */
/*    public static long codeToId(String code) {
        char chs[] = code.toCharArray();
        long res = 0L;
        for (int i = 0; i < chs.length; i++) {
            int ind = 0;
            for (int j = 0; j < binLen; j++) {
                if (chs[i] == r.charAt(j)) {
                    ind = j;
                    break;
                }
            }
            if (chs[i] == b) {
                break;
            }
            if (i > 0) {
                res = res * binLen + ind;
            } else {
                res = ind;
            }
        }
        return res;
    }*/


}
