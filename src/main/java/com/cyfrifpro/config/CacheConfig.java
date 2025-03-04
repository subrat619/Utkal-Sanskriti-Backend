package com.cyfrifpro.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		// This cache manager will create caches with the names provided.
		return new ConcurrentMapCacheManager("templeDetails", "templeAmounts");
	}
}
