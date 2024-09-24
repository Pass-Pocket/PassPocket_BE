package com.mp.passPocket.auth.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mp.passPocket.auth.data.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

	User getByUid(String uid);
}
