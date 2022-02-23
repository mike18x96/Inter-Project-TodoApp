package com.inetum.training.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@EnableCaching
@Profile("springCache")
public class SpringCacheConfig {

    @Bean
    public CacheManager CacheManager() {
        return new ConcurrentMapCacheManager("AllBooks", "OneBook");
    }

}
