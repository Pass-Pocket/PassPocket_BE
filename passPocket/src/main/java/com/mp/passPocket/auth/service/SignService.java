package com.mp.passPocket.auth.service;

import com.mp.passPocket.auth.data.dto.SigninRequest;
import com.mp.passPocket.auth.data.dto.SigninResult;
import com.mp.passPocket.auth.data.dto.SignupRequest;
import com.mp.passPocket.auth.data.dto.SignupResult;
import com.mp.passPocket.auth.data.dto.UpdateRequest;


public interface SignService {

	SignupResult signUp(SignupRequest signupRequest);
	SigninResult signIn(SigninRequest signinRequest) throws RuntimeException;
	SignupResult deleteAccount(SigninRequest delAccRequest);
	SignupResult updateAddress(UpdateRequest updateRequest);

	
	

}
