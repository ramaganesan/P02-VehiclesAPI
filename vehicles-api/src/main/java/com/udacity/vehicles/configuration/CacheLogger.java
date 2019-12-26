package com.udacity.vehicles.configuration;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CacheLogger implements CacheEventListener<Object,Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheLogger.class);

    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        LOGGER.info("Cache event");
        LOGGER.info("Key:EventType:OldValue:NewValue", cacheEvent.getKey(),cacheEvent.getType(),cacheEvent.getOldValue(),cacheEvent.getNewValue());
    }
}
