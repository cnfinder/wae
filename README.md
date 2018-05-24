

project introductions:
   
wae-core:
   核心包

wae-service-rest:
    WAE  服务接口 模块  只提供4个入口 便可用完成所有业务, REST 服务器 同时提供 自动生成 .NET JAVA 平台的SDK JAR 以及调用DEMO ，接口业务处理直接对接WAE Processor， 所以接口建立 发布一般不需要写代码。。。这个很牛逼 哈哈哈
	
wae-wx:
    提供 微信初始状态数据维护 如 token的刷新机制等 以及基础微信处理DEMO，包含 智能机器人聊天
	
wae-cache:
	wae-cache 是一个 支持 本地 和 分布式 的缓存编程模型， 可以轻松的切换 缓存方式， 目前提供 本地缓存 redis缓存 memcached 缓存 实现 ，所有的调用接口都一样 切换缓存模式只需要在XML中配置注入的实现类即可
	
wae-http-commons:
	基于 finder-http-commons HTTP框架 和WAE REST 服务模型实现的客户端HTTP框架

wae-wx-data:
	微信服务号和企业号缓存数据，主要是Token数据，数据由 wae-cache 维护，建议此缓存数据实现分布式
	
	
wx-sdk:
	微信SDK 基于WAE 和 finder-http-commons
	
wae-wx-cache:
	提供小程序缓存实现 基于 wae-cache项目
	
wae-push-framework-client:
	基于ddpush 封装后的 对接WAE 推送消息 客户端框架
	
finder-wae-common-sdk:
	wae的 通用部分的SDK ，当然这个不是必须的 SDK 部分可以自己在后台生成

wae-domain-data:
	wae的对象数据
	
wx-pay:
	微信 支付相关处理
	
	
finder-ddpush-client:
	封装 ddpush-client 
	
wae-global-sdk:
	资源文件 java api sdk开发包

wae-global-core:
	资源业务

wae-global-processor:
	资源服务器 processor 处理项目

wae-aggregation
WAE相关子项目统一管理构建
	
wae-service-rest-data:
    wae-service-rest 项目的 数据对象类
	
