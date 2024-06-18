package top.bulk.mdc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.bulk.mdc.interceptor.LogInterceptor;

/**
 * 配置拦截器生效
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-18
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /**
     * 配置拦截器 bean 注册
     *
     * @return 拦截器
     */
    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    /**
     * 配置拦截器生效得路径
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置 拦截器生效得路径 , /** 表示所有路径
        registry.addInterceptor(logInterceptor()).addPathPatterns("/**");
        //.excludePathPatterns("/bulk-test.html");
    }
}
