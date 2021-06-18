package com.learning.currencyprovider;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CurrencyProviderConfiguration extends CachingConfigurerSupport {

    @Bean
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("recent-rates-cache");
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration.setMaxBytesLocalHeap(5000L);
        cacheConfiguration.setTimeToLiveSeconds(1800);

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfiguration);
        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    @Bean
    public Bucket bandwidth() {
        Bandwidth limit = Bandwidth.classic(2, Refill.greedy(2, Duration.ofSeconds(1)));
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}
