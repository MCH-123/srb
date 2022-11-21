package com.atguigu.srb.core;

import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.entity.Dict;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisTemplateTests
 * @Description TODO
 * @Author mch
 * @Date 2022/11/15
 * @Version 1.0
 */
@SpringBootTest
class RedisTemplateTests {
    @Resource
    private RedisTemplate<String, Dict> redisTemplate;

    @Resource
    private DictMapper dictMapper;
    @Test
    void saveDict() {
        Dict dict = dictMapper.selectById(1);

        redisTemplate.opsForValue().set("dict",dict,5, TimeUnit.MINUTES);
    }
    @Test
    void getDict(){
        Dict dict = redisTemplate.opsForValue().get("dict");
        System.out.println(dict);
    }
}
