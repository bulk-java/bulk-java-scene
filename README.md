# Java业务场景实战

#### 介绍

《Java业务场景实战》提供常见业务场景的最佳解决方案以及优化方案

1. 常见业务场景的企业级解决方案
2. 设计模式在实际业务开发中的应用
3. 真实企业级线上问题排查与调优
4. Java8 在实际项目开发中常用的操作
5. Spring Boot 与常见框架、中间件的集成
6. 提升工作效率的 IDEA 插件推荐以及使用

## 本项目中的场景 case 介绍

### 定制场景 case

| 项目                                                      | 描述                               |
|---------------------------------------------------------|----------------------------------|
| [大文件上传](./scene-customized/big-file-upload)   | 基于boot+vue实现，包含分片上传、断点续传、秒传等功能演示 |
| [根据ip转城市地址](./scene-customized/ip-to-address) | 获取http请求ip，转换成城市地址               |
| [获取节假日](./scene-customized/holidays-case) | 获取一年中那几天是放假的，展示剩余节假日倒计时               |

### 通用场景 case

| 项目                                                  | 描述                                   |
|-----------------------------------------------------|--------------------------------------|
| [幂等场景演示](./scene-general/idempotent-case) | 基于 redis 实现幂等，含自定义注解实现幂等，基于token实现幂等 |
| [MDC+Lockback+TTL链路追踪演示](./scene-general/log-trace-mdc) | Slf4j MDC + Lockback + TTL (阿里 TransmittableThreadLocal) 自定义实现链路追踪能力 |
| [logback 日志脱敏](./scene-general/log-sensitive-masking) | Springboot + logback 演示日志脱敏，Converter、Layout两种方式 |

### 设计模式在实际业务开发中的应用

### Java8 在实际项目开发中常用的操作
