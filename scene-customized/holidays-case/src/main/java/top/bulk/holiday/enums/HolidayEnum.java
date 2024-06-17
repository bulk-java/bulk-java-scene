package top.bulk.holiday.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 节日枚举
 *
 * @author 散装java
 * @date 2022-05-27
 */
@Getter
@AllArgsConstructor
public enum HolidayEnum {
    /**
     * 001 - 元旦节
     * 002 - 春节
     * 003 - 清明节
     * 004 - 劳动节
     * 005 - 端午节
     * 006 - 中秋节
     * 007 - 国庆节
     */
    ONE("001", "元旦"),
    TWO("002", "春节"),
    THREE("003", "清明节"),
    FOUR("004", "劳动节"),
    FIVE("005", "端午节"),
    SIX("006", "中秋节"),
    SEVEN("007", "国庆节"),
    ;
    private final String code;
    private final String desc;
}
