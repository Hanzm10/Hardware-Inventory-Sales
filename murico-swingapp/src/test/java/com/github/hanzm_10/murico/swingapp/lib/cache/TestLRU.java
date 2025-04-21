package com.github.hanzm_10.murico.swingapp.lib.cache;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestLRU {

	@Test
	@DisplayName("Test LRU Cache update()")
	void testCacheUpdate() {
		var cache = new LRU<String, String>(2);
		
		cache.update("foo", "bar");
		
		assertEquals(cache.get("foo"), "bar");
		
		cache.update("deez", "nuts");
		cache.update("say", "what");
		
		assertEquals(cache.get("foo"), null);
	}
	
	@Test
	@DisplayName("Test LRU Cache get()")
	void testCacheGet() {
		var cache = new LRU<String, String>(2);
		
		assertEquals(cache.get("ha"), null);
		
		cache.update("ha", "hatdog");
	
		assertEquals(cache.get("ha"), "hatdog");
	}

}
