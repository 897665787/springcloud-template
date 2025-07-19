package com.company.framework.message.impl;

import com.company.framework.message.IMessage;

public class SimpleMessage implements IMessage {

    @Override
    public String getMessage(String code, Object... args) {
        return code;
    }
}
