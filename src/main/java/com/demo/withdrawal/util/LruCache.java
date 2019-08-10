package com.demo.withdrawal.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K,V> extends LinkedHashMap<K,V>{

	public static final int CACHE_SIZE = 300;

	public LruCache(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}
	
	@Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > CACHE_SIZE;
    }

    public static <K, V> LruCache<K, V> newInstance() {
        return new LruCache<K, V>(CACHE_SIZE, 0.75f, true);
    }

}
