/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.microprofile.showcase.schedule.cdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 *
 * @author mike
 */
@ApplicationScoped
public class CacheProducer {
    
    private CacheManager cm;
    
    @PostConstruct
    public void init() {
        cm = Caching.getCachingProvider().getCacheManager();
    }
    
    @Produces
    @ApplicationScoped
    @ScheduleCache
    public Cache getCache() {
        return cm.getCache("schedule");
    }
    
    @PreDestroy
    public void close() {
        cm.close();
    }
}
