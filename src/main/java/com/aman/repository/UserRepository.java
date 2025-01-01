package com.aman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aman.model.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {

	public boolean existsByEmail(String email);

	public UserDtls findByEmail(String email);
}
