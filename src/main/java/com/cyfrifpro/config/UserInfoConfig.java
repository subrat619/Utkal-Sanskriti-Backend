package com.cyfrifpro.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cyfrifpro.model.User;

public class UserInfoConfig implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long userId; // Store the userId
	private String email;
	private String password;
	private List<GrantedAuthority> authorities;

	public UserInfoConfig(User user) {
		this.userId = user.getUserId(); // Ensure the User model has a getUserId() method
		this.email = user.getEmail();
		this.password = user.getPassword();

		if (user.getRole() == null) {
			throw new IllegalArgumentException("User's Role is missing");
		}
		this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
	}

	public Long getUserId() { // Add getter for userId
		return userId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}
}
