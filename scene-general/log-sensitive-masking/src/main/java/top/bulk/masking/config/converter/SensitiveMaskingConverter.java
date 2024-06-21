package top.bulk.masking.config.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.github.houbb.chars.scan.util.CharsScanPropertyHelper;

/**
 * 手机号脱敏
 *
 * @author 散装java
 * @date 2024-06-20
 */
public class SensitiveMaskingConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        // 在这里实现对手机号码的脱敏逻辑
        String originalMessage = event.getFormattedMessage();
        // 假设手机号码出现在日志信息中，这里简单地将手机号码替换为 "****"
        // String maskedMessage = originalMessage.replaceAll("(1[3-9]\\d)\\d{4}(\\d{4})", "$1****$2");
        // 你可以根据自己的业务规则去处理 originalMessage 内容！
        // 使用开源项目 sensitive-logback 提供的脱敏规则
        String maskedMessage = CharsScanPropertyHelper.scanAndReplace(originalMessage);
        return maskedMessage;
    }
}
