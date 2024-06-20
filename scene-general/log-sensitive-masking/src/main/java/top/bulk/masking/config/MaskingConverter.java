package top.bulk.masking.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 手机号脱敏
 *
 * @author 散装java
 * @date 2024-06-20
 */
public class MaskingConverter  extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        // 在这里实现对手机号码的脱敏逻辑
        String originalMessage = event.getFormattedMessage();
        // 假设手机号码出现在日志信息中，这里简单地将手机号码替换为 "****"
        String maskedMessage = originalMessage.replaceAll("\\b(1\\d{10})\\b", "****");
        return maskedMessage;
    }
}
