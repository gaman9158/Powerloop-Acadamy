package com.aman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aman.model.UserDtls;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private com.aman.repository.UserRepository ur;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Override
	public UserDtls createUser(UserDtls user) {

		user.setPassword(passwordEncode.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		
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

}
