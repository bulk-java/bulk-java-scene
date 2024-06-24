package top.bulk.masking.config.layout;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.github.houbb.chars.scan.util.CharsScanPropertyHelper;

/**
 * 基于 Layout 的方式去进行脱敏类
 *
 * @author 散装java
 * @date 2024-06-21
 */
public class SensitiveMaskingLayout extends PatternLayout {

    @Override
    public String doLayout(ILoggingEvent event) {
        String originalMessage = super.doLayout(event);
        return CharsScanPropertyHelper.scanAndReplace(originalMessage);
    }

}