/**
 * 核心功能：消息驱动，实现异步处理消费者，消费者逻辑放在strategy中，与具体的MQ解耦
 * 建议rocketmq、rabbitmq二选一
 * springevent只是用来不依赖消息中间件启动服务的，只能是单体使用
 */
package com.company.system.messagedriven;
