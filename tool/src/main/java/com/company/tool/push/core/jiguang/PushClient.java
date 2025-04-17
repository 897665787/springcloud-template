package com.company.tool.push.core.jiguang;


import cn.jiguang.sdk.api.PushApi;
import cn.jiguang.sdk.bean.push.PushSendParam;
import cn.jiguang.sdk.bean.push.PushSendResult;
import cn.jiguang.sdk.bean.push.audience.Audience;
import cn.jiguang.sdk.bean.push.message.custom.CustomMessage;
import cn.jiguang.sdk.bean.push.message.notification.NotificationMessage;
import cn.jiguang.sdk.exception.ApiErrorException;
import com.company.tool.push.core.SendResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 极光推送客户端
 *
 * <pre>
 * 官方文档：https://docs.jiguang.cn/jpush/server/push/rest_api_v3_push
 * </pre>
 */
@Slf4j
public class PushClient {

    private PushApi pushApi = null;

    public PushClient(String appKey, String masterSecret) {
        pushApi = new PushApi.Builder().setAppKey(appKey).setMasterSecret(masterSecret).build();
    }

    public SendResponse send(String registrationId, String title, String content, String intentUrl) {
        PushSendParam param = new PushSendParam();
        // 目标人群
        Audience audience = new Audience();
        audience.setRegistrationIdList(Collections.singletonList(registrationId));
        // 指定目标
        param.setAudience(audience);

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setAlert(content);

        NotificationMessage.Android android = new NotificationMessage.Android();
        android.setAlert(content);
        android.setTitle(title);

        NotificationMessage.Android.Intent intent = new NotificationMessage.Android.Intent();
        intent.setUrl(intentUrl);
        android.setIntent(intent);
        notificationMessage.setAndroid(android);

        NotificationMessage.IOS iOS = new NotificationMessage.IOS();
        Map<String, String> iOSAlert = new HashMap<>();
        iOSAlert.put("title", title);
        iOSAlert.put("body", content);
        iOS.setAlert(iOSAlert);
        notificationMessage.setIos(iOS);

        param.setNotification(notificationMessage);

        // 透传消息
        CustomMessage customMessage = new CustomMessage();
        customMessage.setTitle(title);
        customMessage.setContent(content);
        param.setCustom(customMessage);

        SendResponse resp = new SendResponse();
        try {
            PushSendResult response = pushApi.send(param);
            log.info("response:{}", response);
            resp.setRequestId(response.getMessageId());
            resp.setSuccess(true);
        } catch (ApiErrorException e) {
            log.error("ApiErrorException error", e);
            ApiErrorException.ApiError apiError = e.getApiError();
            ApiErrorException.ApiError.Error error = apiError.getError();
            resp.setSuccess(false);
            resp.setMessage(error.getMessage());
            resp.setRequestId("");
        }
        return resp;
    }

    public static void main(String[] args) {
        String apiKey = "";
        String secretKey = "";
        PushClient pushClient = new PushClient(apiKey, secretKey);

        String registrationId = "registrationId";
        String title = "TEST";
        String content = "Hello Baidu push!";
        String intentUrl = "jiguang://platformapi/startapp?appKey=yourAppKey&packageName=yourPackageName&category=yourCategory";
        SendResponse sendResponse = pushClient.send(registrationId, title, content, intentUrl);
        log.info("sendResponse:{}", sendResponse);
    }
}