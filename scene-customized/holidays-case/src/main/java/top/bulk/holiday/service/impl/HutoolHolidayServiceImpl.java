package top.bulk.holiday.service.impl;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import top.bulk.holiday.entity.HolidayResult;
import top.bulk.holiday.service.HolidayService;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取假期服务层实现
 *
 * @author 散装java
 * @date 2022-05-27
 */
@Slf4j
public class HutoolHolidayServiceImpl implements HolidayService {
    /**
     * hutool 的方式 没办法直接获取全年的 假期，只能获取某一天是否是假期
     * 所以这里直接反馈
     * @return HolidayResult
     */
    @Override
    public HolidayResult getHoliday() {
        log.info("基于Hutool实现====>");
        ChineseDate date = new ChineseDate(DateUtil.parseDate("2022-06-03"));
        String festivals = date.getFestivals();
        System.out.println(festivals);
        return new HolidayResult();
    }

}
