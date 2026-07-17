package com.spring.util;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类（Redis 不可用时自动降级为直接查库）
 */
@Component
public class CacheUtil {

    private static final Logger log = LoggerFactory.getLogger(CacheUtil.class);
    private static final long HOME_CACHE_MINUTES = 10;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ==================== 写缓存 ====================

    public void set(String key, Object data, long minutes) {
        if (!isRedisAvailable()) return;
        try {
            redisTemplate.opsForValue().set(key, data, minutes, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Redis 写入失败: {}", e.getMessage());
        }
    }

    public void setHomeCache(String key, Object data) {
        set("home:" + key, data, HOME_CACHE_MINUTES);
    }

    // ==================== 读缓存 ====================

    public String getString(String key) {
        if (!isRedisAvailable()) return null;
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            return obj == null ? null : obj.toString();
        } catch (Exception e) {
            log.warn("Redis 读取失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从缓存读取并解析为 List，Redis 不可用时返回 null 触发查库
     */
    @SuppressWarnings("unchecked")
    public List getList(String key) {
        String json = getString(key);
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JSONArray.parseArray(json);
        } catch (Exception e) {
            log.warn("Redis 数据解析失败: {}", e.getMessage());
            return null;
        }
    }

    public List getHomeCacheList(String module) {
        return getList("home:" + module);
    }

    // ==================== 删缓存 ====================

    public void delete(String key) {
        if (!isRedisAvailable()) return;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis 删除失败: {}", e.getMessage());
        }
    }

    public void deleteHomeCache(String module) {
        delete("home:" + module);
    }

    /**
     * 清除所有首页缓存（数据有任何变更时调用）
     */
    public void clearAllHomeCache() {
        deleteHomeCache("banners");
        deleteHomeCache("hotspots");
        deleteHomeCache("hotfoods");
        deleteHomeCache("hotroutes");
        deleteHomeCache("latestnews");
    }

    // ==================== 内部 ====================

    private boolean isRedisAvailable() {
        try {
            return redisTemplate.getConnectionFactory().getConnection() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
