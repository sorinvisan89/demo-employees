package com.template.demo.config;

import com.template.demo.security.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = {"/v2/api-docs", "/swagger-resources", "/swagger-resources/**",
			"/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/v3/api-docs/**",
			"/swagger-ui/**"};

	private final JWTFilter jwtFilter;

	@Autowired
	public SecurityConfig(final JWTFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().antMatchers("/**").authenticated().and()
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
