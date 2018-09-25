package com.gilmarcarlos.developer.gcursos.security.brute.force;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
 
/**
 * Classe para bloquear ataques de força bruta
 * 
 * @author Gilmar Carlos
 *
 */
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
    
    /**
	 * Método que limpa o cache caso o usuário consiga logar 
	 * 
	 * @param key chave da sessão  
	 * 
	 */
    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }
    
    /**
	 * Método que adiciona ao cache a chave da sessão e quantidades de tentivas de login do usuario 
	 * 
	 * @param key chave da sessão  
	 * 
	 */
    public void loginFailed(String key) {
        int attempts = 0;
        attempts = attemptsCache.get(key);
        attempts++;
        attemptsCache.put(key, attempts);
    }
    
    /**
	 * Método que valida a quantidade de tentativas  
	 * 
	 * @param key chave da sessão  
	 * 
	 */
    public boolean isBlocked(String key) {
        return attemptsCache.get(key) >= MAX_ATTEMPT;
    }
	
}
