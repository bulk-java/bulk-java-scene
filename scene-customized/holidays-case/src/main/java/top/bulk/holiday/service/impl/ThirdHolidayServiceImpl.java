package top.bulk.holiday.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import top.bulk.holiday.entity.HolidayResult;
import top.bulk.holiday.service.HolidayService;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于第三方接口实现
 *
 * @author 散装java
 * @date 2022-05-27
 */
@Slf4j
public class ThirdHolidayServiceImpl implements HolidayService {
    private final String URL = "http://timor.tech/api/holiday/year/2022";

    @Override
    public HolidayResult getHoliday() {
        log.info("基于第三方接口实现===>");
        String s = HttpUtil.get(URL);
        return JSONUtil.toBean(s, HolidayResult.class);
    }
}
