package top.bulk.big.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus 配置
 *
 * @author 散装java
 * @date 2022-11-17
 */
@Configuration
@MapperScan("top.bulk.big.mapper")
public class MybatisConfig {
}
