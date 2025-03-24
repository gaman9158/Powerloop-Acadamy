package com.aman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aman.model.UserDtls;

import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private com.aman.repository.UserRepository ur;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public UserDtls createUser(UserDtls user, String url) {

		user.setEnabled(false); // changed here
		user.setPassword(passwordEncode.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		user.setAccountNonLocked(true);
		user.setFailedAttempt(0);
		user.setLockTime(null);
//		String randomCode = RandomStringUtils.randomAlphanumeric(64); //This code is not working here
		RandomString rn = new RandomString(); // entering the verification code in database from here
		user.setVerificationCode(rn.make(64)); // set the verification code here
		UserDtls us = ur.save(user);
		sendVerificationMail(user, url);
		return ur.save(user);

	}

	@Override
	public boolean checkEmail(String email) {

		return ur.existsByEmail(email);
	}

	@Override
	public UserDtls loginUser(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendVerificationMail(UserDtls user, String url) {
		try {
			String from = "gaman9158.ag@gmail.com";
			String to = user.getEmail();
			String subject = "Account Verification";

			String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
					+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you, <br>"
					+ "Powerloop Acadamy";

			// Replace placeholders with actual values
			content = content.replace("[[name]]", user.getFullName());
			String siteUrl = url + "/verify?code=" + user.getVerificationCode();
			content = content.replace("[[URL]]", siteUrl);

			// Create a MimeMessage
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable HTML

			helper.setFrom(from, "Aman");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true); // true enables HTML

			// Send mail
			mailSender.send(message);
			System.out.println("Verification email sent successfully to: " + to);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean verifyAccount(String code) {

		UserDtls user = ur.findByVerificationCode(code);

		if (user != null) {
			user.setEnabled(true);
			user.setVerificationCode(null);
			ur.save(user);
			return true;
		}

		return false;
	}
	
	

	@Override
	public void increaseFailedAttempt(UserDtls user) {
		int attempt = user.getFailedAttempt() + 1;

		ur.updateFailedAttempt(attempt, user.getEmail());

	}

	@Override
	public void resetAttempt(String email) {
		ur.updateFailedAttempt(0, email);

	}

	@Override
	public void lock(UserDtls user) {
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		ur.save(user);

	}

	long Lock_duration_time = 60000;
	public static final long ATTEMPT_TIME=3;

	@Override
	public boolean unlockAccountTimeExpired(UserDtls user) {
		long lockTimeInMillis = user.getLockTime().getTime();
		long currentTimeMillis = System.currentTimeMillis();

		if (lockTimeInMillis + Lock_duration_time < currentTimeMillis) {
			user.setAccountNonLocked(true);
			user.setLockTime(null);
			user.setFailedAttempt(0);
			ur.save(user);
			return true;
		}
		return false;
	}

}
