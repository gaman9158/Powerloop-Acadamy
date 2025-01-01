package com.aman.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aman.model.UserDtls;

public class CustomUserDetails implements UserDetails {

	private UserDtls user;

	

	// Constructor
	public CustomUserDetails(UserDtls user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// You can return the roles/authorities of the user here
		// If you have roles in UserDtls, map them to GrantedAuthority

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());
		return Arrays.asList(simpleGrantedAuthority); // Replace null with actual roles if applicable
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail(); // Assuming email is used as the username
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Implement as needed
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Implement as needed
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Implement as needed
	}

	@Override
	public boolean isEnabled() {
		return true; // Implement as needed
	}

	// Getters and setters for UserDtls (optional)
	public UserDtls getUser() {
		return user;
	}

	public void setUser(UserDtls user) {
		this.user = user;
	}
}
