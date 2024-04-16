package com.company.web.websocket.dto;

import lombok.Data;

@Data
public class WsMsg {
	String toUserId;
	String message;
}
