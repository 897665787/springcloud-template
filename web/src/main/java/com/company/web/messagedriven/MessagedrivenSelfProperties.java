package com.company.web.messagedriven;

import com.company.framework.messagedriven.properties.Messagedriven;
import com.company.framework.messagedriven.properties.MessagedrivenProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
@ConfigurationProperties(prefix = "messagedriven")
public class MessagedrivenSelfProperties extends MessagedrivenProperties {
    private Messagedriven self;
}
