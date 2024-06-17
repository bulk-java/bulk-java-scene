package top.bulk.holiday.entity;

import lombok.Data;

/**
 * @author 散装java
 * @date 2022-05-27
 */
@Data
public class Holiday {
    /**
     * 是否是假期
     */
    private Boolean holiday;
    private String name;
    /**
     * 薪资倍数
     */
    private Integer wage;
    /**
     * 具体时间
     */
    private String date;
    /**
     * 表示当前时间距离目标还有多少天。比如今天是 2018-09-28，距离 2018-10-01 还有3天
     */
    private Integer rest;
}
