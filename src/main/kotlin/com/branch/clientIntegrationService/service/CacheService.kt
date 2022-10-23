package com.branch.clientIntegrationService.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@EnableScheduling
@EnableCaching
class CacheService(private val cacheManager: CacheManager) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CacheService::class.java)
    }

    fun evictAllCaches() {
        cacheManager.cacheNames.stream()
            .forEach { cacheName: String ->
                cacheManager.getCache(cacheName)!!
                    .clear()
            }
    }

    @Scheduled(fixedRate = 60000)
    fun evictAllCachesAtTime() {
        log.info("evicting caches")
        evictAllCaches()
    }
}
