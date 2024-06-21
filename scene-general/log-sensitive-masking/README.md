# Springboot + logback 演示日志脱敏

> logback 的一些中文资料 https://gitee.com/Marsalis/logback-chinese-manual/tree/master
> 
> logback 在线的中文站点 https://logbackcn.gitbook.io/logback

为了数据安全，对于一些敏感数据的打印，我们希望打印出来的数据是脱敏的

比如用户手机号，打印出来的应该是 130****3210

### 用法

在 logback 的日志脱敏方案中，一般有两种方式

1. 基于 Converter  ，使用 `conversionRule` 标签，去处理输出后的内容，识别其中敏感词，进行脱敏
   - 其中核心为 `top.bulk.masking.config.converter.SensitiveMaskingConverter`
2. 基于 Layout, 自定义个一个 Layout 去做数据的处理
   - 其中核心类为 `top.bulk.masking.config.layout.SensitiveMaskingLayout` 