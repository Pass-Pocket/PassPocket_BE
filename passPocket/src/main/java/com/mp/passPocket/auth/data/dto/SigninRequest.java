package com.mp.passPocket.auth.data.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninRequest {
	String id;
	String password;

}
