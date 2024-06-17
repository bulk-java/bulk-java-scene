package top.bulk.holiday.entity;

import lombok.Data;

import java.util.Map;

/**
 * 节假日实体
 *
 * @author 散装java
 * @date 2022-05-27
 */
@Data
public class HolidayResult {
    /**
     * 返回码
     */
    private String code;
    /**
     * key 是其他 value 是 holiday 类
     */
    private Map<String, Holiday> holiday;

}
