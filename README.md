# blog 三人行博客

[![](https://img.shields.io/badge/blog-三人行博客-red)](http://121.41.59.128/index)
[![](https://img.shields.io/badge/build-SpringSecurity5.3.7%20-green)](https://spring.io/projects/spring-security)
[![](https://img.shields.io/badge/build-minio8.0%2B-blue)](http://docs.minio.org.cn/docs/master/distributed-minio-quickstart-guide)
[![](https://img.shields.io/badge/build-SpringBoot2.3.7-brightgreen)](https://spring.io/projects/spring-boot)

## 登录安全模块 security
 + 使用SpringSecurity进行登录校验支持
 
    登录认证 、权限认证、token验证
   
## 博客模块  service-core
 + 使用redis缓存点赞和评论数据
 
    目前只同步点赞表和评论表中的数据
 + 使用es提供搜索服务
 
    目前只同步blog_info 博客信息表中的数据
## 数据同步
 + 使用canal+rabbitmq 将mysql的binlog日志增量信息发送到rabbitmq,接收消息并同步到redis和es中
## 公共模块 common
 + 一堆工具类

## api模块 
 + 待开放 重构为SpringCloud项目时作为 feign层 用于存放实体类和对外api接口
