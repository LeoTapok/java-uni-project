package com.example.countryinfo.cache;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CacheService<K, V> {

    HashMap<K, V> cache = new HashMap<>();

    private static final int MAX_SIZE = 300;

    public void put(K key, V value) {
        if (cache.size() == MAX_SIZE) {
            clear();
        }
        cache.put(key, value);
    }

    public void clear() {
        cache.clear();
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public V get(K key) {
        return cache.get(key);
    }
}
