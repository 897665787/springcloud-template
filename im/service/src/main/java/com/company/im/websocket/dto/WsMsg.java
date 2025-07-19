package com.company.im.websocket.dto;

import lombok.Data;

@Data
public class WsMsg {
	String toUserId;
	String message;
}
