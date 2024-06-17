package top.bulk.holiday;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author 散装Java
 * @since 2022/5/29
 */
@Slf4j
public class HolidayApplicationTest {
    @Test
    public void test() {
        log.info("基于Hutool实现====>");
        ChineseDate date = new ChineseDate(DateUtil.parseDate("2022-06-03"));
        String festivals = date.getFestivals();
        log.info(festivals);
    }
}
