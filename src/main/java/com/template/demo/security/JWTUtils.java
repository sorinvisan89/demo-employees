package com.template.demo.security;

import com.template.demo.domain.model.UserInfo;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class JWTUtils {

	private final String secret;

	@Autowired
	public JWTUtils(@Value("${jwt.secret}") final String secret) {
		this.secret = secret;
	}

	public UserInfo decodeToken(final String jwtToken) {

		final Jws<Claims> jws = Jwts.parser().setSigningKey(this.secret.getBytes(StandardCharsets.UTF_8))
				.parseClaimsJws(jwtToken);

		final Integer userId = jws.getBody().get("user_id", Integer.class);
		final String sessionId = jws.getBody().get("session_id", String.class);
		@SuppressWarnings("unchecked")
		final List<String> roles = jws.getBody().get("roles", List.class);

		return UserInfo.builder().userId(userId).sessionId(sessionId).roles(roles).build();
	}
}
