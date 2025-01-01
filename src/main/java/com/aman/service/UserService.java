package com.aman.service;

import com.aman.model.UserDtls;

public interface UserService {

	public UserDtls createUser(UserDtls user);

	public boolean checkEmail(String email);
	
	public UserDtls loginUser(String email);


}
