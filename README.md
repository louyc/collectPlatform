# collectPlatform
之前公司前辈 搭建的采集平台 框架是OSGI框架 面向模块开发的
np.cmpnt.framework.arsf 开头的 是封装的底层基础服务  提供基础功能
gd.gbw.services.cp 开头的 是业务功能开发

涉及 多线程 高并发 阻塞队列 优先级处理 mq jms消息等  


JMSTaskProcesser.java (jms消息入口) 模块间用jms消息订阅传递

