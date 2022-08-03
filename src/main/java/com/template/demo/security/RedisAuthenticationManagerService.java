package com.template.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class RedisAuthenticationManagerService {

	private final RedisIndexedSessionRepository redisSessionRepository;

	@Autowired
	public RedisAuthenticationManagerService(final RedisIndexedSessionRepository redisSessionRepository) {
		this.redisSessionRepository = redisSessionRepository;
	}

	public boolean authenticate(final String sessionId, final String jwtToken) {

		final Session redisSession = redisSessionRepository.findById(sessionId);

		if (redisSession == null) {
			return false;
		}
		final String userStoredJWT = redisSession.getAttribute("USER_INFO");
		return jwtToken.equals(userStoredJWT);
	}
}
