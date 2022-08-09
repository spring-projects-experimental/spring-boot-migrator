package org.springboot.example.ehcache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

public class EventLogger implements CacheEventListener<Object, Object> {
    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        System.out.println("My ehcache event listener is called");
    }
}
