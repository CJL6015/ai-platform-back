package com.seu.platform.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-04-06 18:59
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheTask {
    private final CacheManager cacheManager;

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearCacheAtMidnight() {
        cacheManager.getCacheNames().forEach(cacheName ->
                Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
        System.out.println("缓存已清除");
    }
}
