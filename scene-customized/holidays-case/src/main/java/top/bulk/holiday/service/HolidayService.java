package top.bulk.holiday.service;

import top.bulk.holiday.entity.HolidayResult;
import top.bulk.holiday.entity.HolidayView;

import java.util.List;

/**
 * 获取假期服务层接口服务层
 *
 * @author 散装java
 * @date 2022-05-27
 */
public interface HolidayService {
    /**
     * 获取假期方法
     */
    HolidayResult getHoliday();

    /**
     * 获取 今年剩下的节假日
     *
     * @return list
     */
    default List<HolidayView> getNextHolidays() {
        return null;
    }
}
