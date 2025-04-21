package com.company.tool.push.core.firebase;


import com.company.tool.push.core.Constants;
import com.company.tool.push.core.SendResponse;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * firebase推送客户端
 *
 * <pre>
 * 官方文档：https://firebase.google.com/docs/admin/setup?hl=zh-cn#java
 * </pre>
 */
@Slf4j
public class PushClient {

    private FirebaseApp firebaseApp = null;

    public PushClient(String accountFilePath) {
        GoogleCredentials googleCredentials = null;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(accountFilePath)) {
            googleCredentials = GoogleCredentials.fromStream(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FirebaseOptions options = FirebaseOptions.builder().setCredentials(googleCredentials).build();
        firebaseApp = FirebaseApp.initializeApp(options);
    }

    public SendResponse send(String token, String title, String body, String intent, Constants.MessageType messageType) {
        Message.Builder builder = Message.builder().setToken(token);
        if (messageType == Constants.MessageType.TRANSPORT) {
            // 透传消息
            Map<String, String> data = new HashMap<>();
            data.put("intent", intent);
            data.put("title", title);
            data.put("body", body);

            builder.putAllData(data);
        } else if (messageType == Constants.MessageType.NOTICE) {
            // 通知消息
            Notification notification = Notification.builder().setTitle(title).setBody(body).build();

            Map<String, String> data = new HashMap<>();
            data.put("intent", intent);

            builder.setNotification(notification).putAllData(data);
        } else {
            throw new IllegalArgumentException("messageType is not support");
        }

        Message message = builder.build();
        SendResponse resp = new SendResponse();
        try {
            FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FirebaseMessagingException error", e);
            MessagingErrorCode messagingErrorCode = e.getMessagingErrorCode();
            resp.setSuccess(false);
            resp.setMessage("errorCode:" + messagingErrorCode.name());
            resp.setRequestId("");
        }
        return resp;
    }

    public static void main(String[] args) {
        String accountFilePath = "config-file/firebase-service-account.json";
        PushClient pushClient = new PushClient(accountFilePath);

        String token = "token";
        String title = "TEST";
        String content = "Hello Baidu push!";
        String intent = "path/to/page";
        Constants.MessageType messageType = Constants.MessageType.NOTICE;
        SendResponse sendResponse = pushClient.send(token, title, content, intent, messageType);
        log.info("sendResponse:{}", sendResponse);

    }
}