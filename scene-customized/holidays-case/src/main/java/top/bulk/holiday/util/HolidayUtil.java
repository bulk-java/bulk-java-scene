package top.bulk.holiday.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import top.bulk.holiday.entity.Holiday;
import top.bulk.holiday.entity.HolidayResult;
import top.bulk.holiday.entity.HolidayView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 假期类工具类
 *
 * @author 散装java
 * @date 2022-05-27
 */
public class HolidayUtil {

    /**
     * 获取今年剩余的假期
     *
     * @param result 今年全部的假期列表
     * @return
     */
    public static ArrayList<HolidayView> getNextHoliday(HolidayResult result) {
        Map<String, Holiday> map = result.getHoliday();
        Set<String> strings = map.keySet();
        final String now = LocalDateTimeUtil.format(LocalDateTime.now(), "MM-dd");
        // 获取今天之后的节日
        List<String> nextHolidays = strings.stream()
                .filter(item -> item.compareTo(now) > 0)
                .sorted()
                .collect(Collectors.toList());
        LinkedList<Holiday> holidays = new LinkedList<>();
        nextHolidays.forEach(key -> {
            holidays.add(map.get(key));
        });
        ArrayList<HolidayView> views = new ArrayList<>();
        holidays.forEach(item -> {
            if (item.getHoliday()) {
                HolidayView holidayView = new HolidayView();
                holidayView.setName(item.getName());
                // 计算时间
                String start = LocalDateTimeUtil.format(LocalDate.now(), DateTimeFormatter.ISO_DATE);
                long between = LocalDateTimeUtil.between(LocalDateTimeUtil.parse(start, DateTimeFormatter.ISO_DATE), LocalDateTimeUtil.parse(item.getDate(), DateTimeFormatter.ISO_DATE), ChronoUnit.DAYS);
                holidayView.setDays(between);
                holidayView.setDate(item.getDate());
                // 对节日去重添加
                if (views.isEmpty()) {
                    views.add(holidayView);
                } else {
                    AtomicInteger a = new AtomicInteger(0);
                    views.forEach(data -> {
                        if (data.getName().equals(holidayView.getName())) {
                            a.addAndGet(1);
                        }
                    });
                    if (a.get() == 0) {
                        views.add(holidayView);
                    }
                }
            }
        });
        return views;
    }
}
