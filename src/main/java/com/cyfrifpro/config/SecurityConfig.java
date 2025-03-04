package com.cyfrifpro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cyfrifpro.service.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JWTFilter jwtFilter;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Bean
	public SecurityFilterChain securityChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> {
			requests.requestMatchers(AppConstants.PUBLIC_URLS).permitAll();
			AppConstants.ROLE_URLS.forEach((role, urls) -> requests.requestMatchers(urls).hasAuthority(role.name()));
			requests.anyRequest().authenticated();
		}).exceptionHandling(handling -> handling.authenticationEntryPoint((request, response,
				authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))

				// Stateless session management
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// Add JWT filter before username-password authentication filter
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		// Use custom DAO authentication provider
		http.authenticationProvider(daoAuthenticationProvider());

		// Build and return the security filter chain
		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		// Use UserDetailsServiceImpl for the UserDetailsService
		provider.setUserDetailsService(userDetailsServiceImpl);

		// Set the password encoder
		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}