package com.cyfrifpro.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

	private SecurityUtils() {
		// Prevent instantiation
	}

	public static Long getLoggedInUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal())) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof Long) {
			return (Long) principal;
		} else if (principal instanceof String) {
			// If the principal is a String, try to convert it to Long (if applicable)
			try {
				return Long.valueOf((String) principal);
			} catch (NumberFormatException e) {
				// If it cannot be converted, return null or throw an exception as needed.
				return null;
			}
		}
		// Optionally, handle cases where the principal is a UserDetails instance
		// e.g., if you have a custom UserDetails that includes a userId field.
		return null;
	}
}
