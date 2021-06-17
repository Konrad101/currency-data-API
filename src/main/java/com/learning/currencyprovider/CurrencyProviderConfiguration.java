package com.learning.currencyprovider;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CurrencyProviderConfiguration {

    @Bean
    public RateLimiter rateLimiter(){
        return RateLimiter.create(2, Duration.ofSeconds(15));
    }
}
