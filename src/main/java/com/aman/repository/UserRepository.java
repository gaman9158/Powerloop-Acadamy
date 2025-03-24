package com.aman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aman.model.UserDtls;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {
	
	public boolean existsByEmail(String email);
	
	public UserDtls findByEmail(String email);
	
	public UserDtls findByEmailAndMobileNumber(String email, String mobileNumber);
	
	public UserDtls findByVerificationCode(String Code);
	
	@Transactional
	@Modifying
	@Query("update UserDtls u set u.failedAttempt=?1 where email=?2")
	public void updateFailedAttempt(int attempt, String email);
	
}
 