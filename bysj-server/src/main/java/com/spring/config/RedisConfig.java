package com.spring.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 * 配置 RedisTemplate 用 Fastjson 序列化 Value，避免 JDK 序列化的可读性问题
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Key 用字符串序列化
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value 用 Fastjson 序列化（可读、跨语言）
        template.setValueSerializer(new FastjsonRedisSerializer());
        template.setHashValueSerializer(new FastjsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 基于 Fastjson 实现 Redis 序列化器
     * 存入 Redis 的 Value 是 JSON 字符串，可读可调试
     */
    private static class FastjsonRedisSerializer implements RedisSerializer<Object> {

        @Override
        public byte[] serialize(Object obj) {
            if (obj == null) {
                return new byte[0];
            }
            // 如果是字符串，直接 getBytes，不加引号
            if (obj instanceof String) {
                return obj.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
            }
            return JSON.toJSONString(obj).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }

        @Override
        public Object deserialize(byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            String str = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
            // 尝试判断是数组还是对象
            str = str.trim();
            if (str.startsWith("[")) {
                return JSONArray.parseArray(str);
            }
            return str;
        }
    }
}
