package top.bulk.inter.annotation;

import top.bulk.inter.enums.LogOptTypeEnum;

import java.lang.annotation.*;

/**
 * 记录接口日志注解
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-25
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BizLog {
    /**
     * 业务的名称,例如:"修改菜单"
     */
    String title() default "";

    /**
     * 日志操作类型
     */
    LogOptTypeEnum optType() default LogOptTypeEnum.OTHER;

}
