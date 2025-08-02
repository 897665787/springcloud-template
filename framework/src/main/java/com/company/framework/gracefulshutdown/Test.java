package com.company.framework.gracefulshutdown;

import cn.hutool.http.HttpUtil;

public class Test {
    public static void main(String[] args) {
        String url = "http://localhost:6001/feignTest/getparam?orderCode=";

        int min = 1000;
        int max = 8000;
        boolean flag = true;
        for (int i = min; i < max; i++) {
            if ((min + (min + max) / 10 <= i && flag)) {
                flag = false;
                new Thread(() -> {
                    String url2 = "http://localhost:8002/preStop";
                    String s2 = HttpUtil.get(url2);
//                    String url2 = "http://localhost:8002/actuator/shutdown";
//                    String s2 = HttpUtil.post(url2, Maps.newHashMap());
                    System.out.println(s2);
                }).start();
            }
            String s = HttpUtil.get(url + i);
            System.out.println(s);
        }
    }
}
