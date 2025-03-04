package com.cyfrifpro.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cyfrifpro.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Extract the Authorization header
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7); // Remove "Bearer " prefix

			if (jwt.isBlank()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer Header");
				return;
			} else {
				try {
					// Retrieve claims from the JWT token
					Map<String, Object> claims = jwtUtil.validateTokenAndRetrieveClaims(jwt);

					// Extract individual claims (userId, email, roleName)
					Long userId = ((Number) claims.get("userId")).longValue(); // Safe conversion for userId
					String email = (String) claims.get("email");
					String role = (String) claims.get("role");

					// Load user details from the database using email
					UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
					System.out.println(userDetails);

					// Create a granted authority based on the roleName
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

					// Create the authentication token using userId and authority
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userId, null, List.of(authority));

					// Set the authentication in the security context if it's not already set
					if (SecurityContextHolder.getContext().getAuthentication() == null) {
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					}

					// Attach userId to the request for further use (optional)
					request.setAttribute("userId", userId);

				} catch (JWTVerificationException e) {
					// Handle expired token specifically
					if (e.getMessage().contains("The Token has expired")) {
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Your session has been expired");
						return;
					} else {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
						return;
					}
				} catch (ClassCastException | NullPointerException e) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token Structure");
					return;
				}
			}
		}

		// Proceed to the next filter in the chain
		filterChain.doFilter(request, response);
	}
}