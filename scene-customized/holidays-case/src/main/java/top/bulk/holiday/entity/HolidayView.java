package top.bulk.holiday.entity;

import lombok.Data;

/**
 * 前端显示使用实体
 *
 * @author 散装java
 * @date 2022-05-27
 */
@Data
public class HolidayView {
    /**
     * 名字
     */
    private String name;
    /**
     * 当前距离节日还有多少天
     */
    private Long days;
    /**
     * 放假开始时间
     */
    private String date;
}
