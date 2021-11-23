package com.threeman.servicecore.config.redis;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * @author songjing
 * @version 1.0
 * @date 2021/11/22 15:35
 */
@Slf4j
public class RedisCache implements Cache {

    private final String id;

    private final RedisTemplate<String,Object> redisTemplate;

    public RedisCache(String id, RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        log.info("当前的缓存id: [{}]",id);
        this.id =id ;
    }



    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        log.info("放入缓存key:[{}] 放入缓存的value:[{}]",key,value);
        redisTemplate.opsForValue().set(key.toString(),value);
    }

    @Override
    public Object getObject(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return redisTemplate.delete(key.toString());
    }

    @Override
    public void clear() {
        redisTemplate.execute((RedisCallback) connection -> {
            connection.flushDb();
            return null;
        });
        log.info("redis刷新成功");
    }

    @Override
    public int getSize() {
        return 0;
    }
}
