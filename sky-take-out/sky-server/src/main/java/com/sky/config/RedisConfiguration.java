package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建redis模板对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置redis的连接工厂对象（starter自己创建）
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置redis的key的序列化器
        // 如果不设置默认使用的是jdk的序列化器
        // 可读性差：在 RDM 或 redis-cli 中查看时，Key 看起来像乱码。
        // 跨语言不通用：它是 Java 特有的格式。如果你以后用 Python 或 Go 读这个数据，它们识别不出这种 Java 序列化后的 Key。
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
