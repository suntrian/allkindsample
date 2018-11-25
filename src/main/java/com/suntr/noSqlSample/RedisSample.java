package com.suntr.noSqlSample;

import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisSample {


    private static Jedis jedis;

    /**
     * @see RedisSample
     * @param args
     */
    public static void main(String[] args) {
        jedis = new Jedis("172.19.84.151");
        jedis.auth("123456");
        jedis.lpush("hello", "world");
        jedis.rpush("hello","liman");

        List<String> list = jedis.lrange("hello",0,2);
        list.forEach(System.out::println);
    }
}
