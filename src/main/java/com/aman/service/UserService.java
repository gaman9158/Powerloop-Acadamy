package com.aman.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.aman.model.UserDtls;

public interface UserService {

	public UserDtls createUser(UserDtls user,String url);

	public boolean checkEmail(String email);
	
	public UserDtls loginUser(String email);

	public boolean verifyAccount(String code);
	
	public void increaseFailedAttempt(UserDtls user);

	public void resetAttempt(String email);

	public void lock(UserDtls user);

	public boolean unlockAccountTimeExpired(UserDtls user);


}
