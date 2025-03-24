package com.aman.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.aman.model.UserDtls;
import com.aman.repository.UserRepository;
import com.aman.service.UserService;
import com.aman.service.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserService userService;

	@Autowired
	UserRepository userRepo;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String email = request.getParameter("username");
		UserDtls user = userRepo.findByEmail(email);

		if (user != null) {
			if (user.isAccountNonLocked()) {
				if (user.getFailedAttempt() < UserServiceImpl.ATTEMPT_TIME - 1) {
					userService.increaseFailedAttempt(user);
				} else {
					userService.lock(user);
					exception = new LockedException("Your account is locked !! failed attempt 3 times");
				}
			} else if (!user.isAccountNonLocked()) {
				if (userService.unlockAccountTimeExpired(user)) {
					exception = new LockedException("Account is unlocked, Please try to login");
				} else {
					exception = new LockedException("Account is locked, Please try after sometime");
				}
			} else {
				exception = new LockedException("account is inactive, verify account");
			}
		}
		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}
}
