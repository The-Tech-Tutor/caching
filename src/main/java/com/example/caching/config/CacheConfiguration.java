package com.example.caching.config;

import com.example.caching.constant.CacheConstants;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Data
@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CaffeineCache databaseCache() {
        return new CaffeineCache(CacheConstants.DATABASE_CACHE, Caffeine.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .build());
    }
}
