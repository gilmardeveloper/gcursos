package com.gilmarcarlos.developer.gcursos.security;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
 
@Service
public class AutenticacaoBlockForcaBruta{

	private final int MAX_ATTEMPT = 5;
    private LoadingCache<String, Integer> attemptsCache;
 
    public AutenticacaoBlockForcaBruta() {
        super();
        attemptsCache = Caffeine.newBuilder().
          expireAfterWrite(15, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }
 
    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }
 
    public void loginFailed(String key) {
        int attempts = 0;
        attempts = attemptsCache.get(key);
        attempts++;
        attemptsCache.put(key, attempts);
    }
 
    public boolean isBlocked(String key) {
        return attemptsCache.get(key) >= MAX_ATTEMPT;
    }
	
}
