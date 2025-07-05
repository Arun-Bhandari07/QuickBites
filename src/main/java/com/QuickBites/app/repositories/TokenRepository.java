package com.QuickBites.app.repositories;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepository {
	
	@Value("${jwt.accessKey.expiration.time}")
	private Long jwtExpiration;
	
	@Value("${jwt.refreshKey.expiration.time}")
	private Long refreshExpiration;

	private final RedisTemplate<String,Object> redisTemplate;
	
	public TokenRepository(RedisTemplate<String,Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public static final String ACCESS_TOKEN_KEY_PREFIX = "user:access:";
	public static final String REFRESH_TOKEN_KEY_PREFIX = "user:refresh:";
	
	
	public static final String ACCESS_TOKEN_KEY_BLACKLIST_PREFIX = "blacklist:access:";
	public static final String REFRESH_TOKEN_KEY_BLACKLIST_PREFIX = "blacklist:refresh:";
	

	public void storeToken(String name, String accessToken , String refreshToken) {
		//Store accessToken
		String accessKey = ACCESS_TOKEN_KEY_PREFIX.concat(name);
		storeToken(accessKey,accessToken,jwtExpiration);
		//Store refreshToken
		String refreshKey = REFRESH_TOKEN_KEY_PREFIX.concat(name);
		storeToken(refreshKey,refreshToken,refreshExpiration);		
	}
	
	public String getAccessKeyToken(String username) {
		String accessKey = ACCESS_TOKEN_KEY_PREFIX.concat(username);
		Object accessToken = redisTemplate.opsForValue().get(accessKey);
		return accessToken!=null?accessToken.toString():null;
	}
	
	
	public String getRefreshKeyToken(String username) {
		String refreshKey = REFRESH_TOKEN_KEY_PREFIX.concat(username);
		Object refreshToken = redisTemplate.opsForValue().get(refreshKey);
		return refreshToken!=null?refreshToken.toString():null;
	}
	
	
	
	public void storeToken(String key,String token , Long expiry) {
		 redisTemplate.opsForValue().set(key, token);
		 redisTemplate.expire(key,expiry,TimeUnit.MILLISECONDS);
		 
	}
	
	public void removeAllToken(String username) {
		String accessToken = getAccessKeyToken(username);
		String refreshToken = getRefreshKeyToken(username);
		
		String accessKey = ACCESS_TOKEN_KEY_PREFIX.concat(username);
		String refreshKey = REFRESH_TOKEN_KEY_PREFIX.concat(username);
		redisTemplate.delete(accessKey);
		redisTemplate.delete(refreshKey);
		
		if(accessToken!=null) {
			String accessBlackListKey = ACCESS_TOKEN_KEY_BLACKLIST_PREFIX+accessToken;
			blackListToken(accessBlackListKey,jwtExpiration);
		}
		if(refreshToken!=null) {
			String refreshBlackListKey = REFRESH_TOKEN_KEY_BLACKLIST_PREFIX+refreshToken;
			blackListToken(refreshBlackListKey,refreshExpiration);
		}
		
	
	}

	public void blackListToken(String accessBlackListKey, Long jwtExpiration) {
		redisTemplate.opsForValue().set(accessBlackListKey, "BlackListed");
		redisTemplate.expire(accessBlackListKey, jwtExpiration,TimeUnit.MILLISECONDS);
		
	}
	
	public boolean isAccessTokenBlackListed(String accessToken) {
		String key = ACCESS_TOKEN_KEY_BLACKLIST_PREFIX+accessToken;
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
	
	public boolean isRefreshTokenBlackListed(String refreshToken) {
		String key = REFRESH_TOKEN_KEY_BLACKLIST_PREFIX+refreshToken;
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	public void removeAccessToken(String username) {
		String accessToken = getAccessKeyToken(username);
		String key= ACCESS_TOKEN_KEY_PREFIX.concat(username);
		redisTemplate.delete(key);
		

		if(accessToken!=null) {
			String accessBlackListKey = ACCESS_TOKEN_KEY_BLACKLIST_PREFIX+accessToken;
			blackListToken(accessBlackListKey,jwtExpiration);
		}
		
	}
	
}
