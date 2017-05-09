package com.flockinger.poppynotes.userService.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flockinger.poppynotes.userService.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByAuthEmail(String authEmail);
}
