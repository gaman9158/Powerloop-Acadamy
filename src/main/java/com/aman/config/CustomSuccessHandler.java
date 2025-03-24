package com.aman.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.aman.model.UserDtls;
import com.aman.service.UserService;

import jakarta.servlet.ServletException;

@Configuration
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		CustomUserDetails customUser = (CustomUserDetails) request.getUserPrincipal();
		UserDtls user = customUser.getUser();

		if (user != null) {
			userService.resetAttempt(user.getEmail());
		}

		if (roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/profile");
		} else if (roles.contains("ROLE_TEACHER")) {
			response.sendRedirect("/teacher/");
		} else {
			response.sendRedirect("/user/profile");
		}

	}
}