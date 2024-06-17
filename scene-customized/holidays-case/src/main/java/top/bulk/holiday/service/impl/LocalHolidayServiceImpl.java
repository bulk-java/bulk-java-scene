package top.bulk.holiday.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import top.bulk.holiday.entity.HolidayResult;
import top.bulk.holiday.entity.HolidayView;
import top.bulk.holiday.service.HolidayService;
import top.bulk.holiday.util.HolidayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 本地实现
 *
 * @author 散装java
 * @date 2022-05-27
 */
@Slf4j
@Service
public class LocalHolidayServiceImpl implements HolidayService {
    @Override
    public HolidayResult getHoliday() {
        log.info("基于本地实现====>");
        File file = FileUtil.file(System.getProperty("user.dir") + "/data/2022.json");
        JSON json = JSONUtil.readJSON(file, Charset.defaultCharset());
        return json.toBean(HolidayResult.class);
    }

    @Override
    public List<HolidayView> getNextHolidays() {
        HolidayResult holiday = this.getHoliday();
        return HolidayUtil.getNextHoliday(holiday);
    }
}
