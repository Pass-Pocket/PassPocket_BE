package com.mp.passPocket.auth.data.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mp.passPocket.auth.data.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupRequest {
	String id;
	String password;
	String name;
	String phone;
	String address;
	
	@JsonIgnore
	public PasswordEncoder passwordEncoder;

	
	 public User toEntity(PasswordEncoder passwordEncoder) {
		 this.passwordEncoder = passwordEncoder;
		 
	        return User.builder()
	        		.uid(id)
	        		.password(passwordEncoder.encode(password))
	        		.name(name)
	        		.phone(phone)
	        		.address(address)
	        		.build();
	 }
}
