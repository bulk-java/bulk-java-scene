package top.bulk.inter.entity;

import lombok.Data;
import lombok.ToString;
import top.bulk.inter.enums.LogOptTypeEnum;

import java.time.LocalDateTime;

/**
 * 业务操作日志对象
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-25
 */
@Data
@ToString
public class BizOptLog {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String title;

    /**
     * 操作类型
     *
     * @see LogOptTypeEnum
     */
    private String optType;

    /**
     * 是否执行成功（是，否）
     */
    private String success;

    /**
     * 具体消息
     */
    private String message;

    /**
     * ip
     */
    private String ip;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 类名称
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 请求方式(GET POST PUT DELETE)
     */
    private String reqMethod;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 返回结果
     */
    private String result;

    private Long costTime;

    /**
     * 操作时间
     */
    private LocalDateTime optTime;

    /**
     * 操作人
     */
    private String account;

}
