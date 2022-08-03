package com.template.demo.security;

import com.template.demo.domain.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {

	private static final String TOKEN_HEADER = "authorization";
	private static final String BEARER = "Bearer ";

	private final JWTUtils jwtUtils;
	private final RedisAuthenticationManagerService redisAuthenticationManagerService;

	@Autowired
	public JWTFilter(final JWTUtils jwtUtils,
			final RedisAuthenticationManagerService redisAuthenticationManagerService) {
		this.jwtUtils = jwtUtils;
		this.redisAuthenticationManagerService = redisAuthenticationManagerService;
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader(TOKEN_HEADER);

		if (requestTokenHeader == null || !requestTokenHeader.startsWith(BEARER)) {
			log.warn("No authorization present in request!");
			SecurityContextHolder.clearContext();
			filterChain.doFilter(request, response);
			return;
		}

		final String jwtToken = requestTokenHeader.substring(BEARER.length());
		final UserInfo userInfo = jwtUtils.decodeToken(jwtToken);

		final String sessionId = userInfo.getSessionId();
		final boolean redisAuthenticated = redisAuthenticationManagerService.authenticate(sessionId, jwtToken);
		if (!redisAuthenticated) {
			log.warn("No session {} present in Redis!", sessionId);
			SecurityContextHolder.clearContext();
			filterChain.doFilter(request, response);
			return;
		}

		final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userInfo.getUserId(),
				null, buildAuthorities(userInfo));
		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(final HttpServletRequest request) {
		String path = request.getServletPath();
		return !path.startsWith("/employees");
	}

	private List<SimpleGrantedAuthority> buildAuthorities(final UserInfo userInfo) {
		final List<String> roles = Optional.ofNullable(userInfo.getRoles()).orElse(Collections.emptyList());
		return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
}
