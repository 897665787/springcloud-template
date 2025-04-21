package com.company.tool.push.core.baidu;


import com.alibaba.fastjson.JSON;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.company.tool.push.core.SendResponse;
import com.company.tool.push.core.baidu.dto.AndroidMsgStruct;
import com.company.tool.push.core.baidu.dto.IOSMsgStruct;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 百度云推送客户端
 *
 * <pre>
 * 官方文档：https://docs.jiguang.cn/jpush/server/push/rest_api_v3_push
 * </pre>
 */
@Slf4j
public class PushClient {

    private BaiduPushClient pushClient = null;

    public PushClient(String apiKey, String secretKey) {
        PushKeyPair pair = new PushKeyPair(apiKey, secretKey);
        pushClient = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);

        // 注册YunLogHandler，获取本次请求的交互信息
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                log.info("event:{}", JSON.toJSONString(event));
            }
        });
    }

    public SendResponse send(String channelId, Integer deviceType, String title, String content, String intent, Integer msgType) {
        String message;
        if (deviceType == 3) { // Android
            AndroidMsgStruct msgStruct = new AndroidMsgStruct();
            msgStruct.setTitle(title);
            msgStruct.setDescription(content);
            msgStruct.setPkgContent(intent);
            message = JSON.toJSONString(msgStruct);
        } else if (deviceType == 4) {// IOS
            IOSMsgStruct msgStruct = new IOSMsgStruct();
            IOSMsgStruct.Aps aps = new IOSMsgStruct.Aps();
            IOSMsgStruct.Aps.Alert alert = new IOSMsgStruct.Aps.Alert();
            alert.setTitle(title);
            alert.setBody(content);
            aps.setAlert(alert);
            msgStruct.setAps(aps);
            message = JSON.toJSONString(msgStruct);
        } else {
            throw new IllegalArgumentException("不支持的deviceType: " + deviceType);
        }

        PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest()//
                .addChannelId(channelId)// 唯一对应一台设备，必须为客户端初始化成功之后返回的channelId，默认null。
                .addMessage(message).addMsgExpires(3600 * 5) // 相对于当前时间的消息过期时间，单位为秒，取值：(0, 86400 x 7]，默认值为3600 x 5
                .addMessageType(msgType) // 消息类型,0：透传消息 1：通知 默认值为0
                .addDeviceType(deviceType)// 设备类型，3：Android，4：IOS
                ;

        SendResponse resp = new SendResponse();
        try {
            PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);
            log.info("response:{}", response);
            resp.setRequestId(response.getMsgId());
            resp.setSuccess(true);
        } catch (PushClientException e) {
            log.error("PushClientException error", e);
            resp.setSuccess(false);
            resp.setMessage(e.getMessage());
            resp.setRequestId("");
        } catch (PushServerException e) {
            log.error("PushServerException error", e);
            resp.setSuccess(false);
            resp.setMessage(Optional.ofNullable(e.getErrorMsg()).orElse(e.getMessage()));
            resp.setRequestId(String.valueOf(e.getRequestId()));
        }
        return resp;
    }

    public static void main(String[] args) {
        String apiKey = "apiKey";
        String secretKey = "secretKey";
        PushClient pushClient = new PushClient(apiKey, secretKey);

        String channelId = "channelId";
        Integer deviceType = 3;
        String title = "TEST";
        String content = "Hello Baidu push!";
        String intent = "path/to/page";
        Integer messageType = 1; // 0：透传消息 1：通知
        SendResponse sendResponse = pushClient.send(channelId, deviceType, title, content, intent, messageType);
        log.info("sendResponse:{}", sendResponse);
    }
}