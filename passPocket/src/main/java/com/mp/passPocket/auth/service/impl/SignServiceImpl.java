package com.mp.passPocket.auth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mp.passPocket.auth.data.dto.SigninRequest;
import com.mp.passPocket.auth.data.dto.SigninResult;
import com.mp.passPocket.auth.data.dto.SignupRequest;
import com.mp.passPocket.auth.data.dto.SignupResult;
import com.mp.passPocket.auth.data.dto.UpdateRequest;
import com.mp.passPocket.auth.data.entity.User;
import com.mp.passPocket.auth.data.repository.UserRepository;
import com.mp.passPocket.auth.service.SignService;
import com.mp.passPocket.common.dto.ApiResponse;
import com.mp.passPocket.config.security.JwtTokenProvider;

@Service
public class SignServiceImpl implements SignService{
	
	private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

	public UserRepository userRepository;
	public PasswordEncoder passwordEncoder;
	public JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	public SignServiceImpl(UserRepository userRepository,
			JwtTokenProvider jwtTokenProvider,
			PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public SignupResult signUp(SignupRequest req) {
		LOGGER.info("[getSignUpResult] 회원 가입 정보 전달");
		
		User savedUser = userRepository.save(req.toEntity(passwordEncoder));
		SignupResult signUpResultDto = new SignupResult();
		
		if(!savedUser.getName().isEmpty()) {
			setSuccessResult(signUpResultDto, savedUser);
			LOGGER.info("[getSignUpResult] 정상처리 완료");
		} else {
			setFailResult(signUpResultDto, new Object());
			LOGGER.info("[getSignUpResult] 실패처리 완료");
		}
		return signUpResultDto;
		
	}
	
	

	
	@Override
	public SigninResult signIn(SigninRequest signinRequest) throws RuntimeException {
		LOGGER.info("[SignIn] 로그인 정보 전달 {} / {}",signinRequest.getId(),signinRequest.getPassword());
		User user = userRepository.getByUid(signinRequest.getId());
		
		LOGGER.info("[SignIn] 아이디로 회원 조회 {} / {}",user.getUid() , user.getPassword());
		
		if(!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
			LOGGER.info("[getSignInResult] 틀린 비밀번호");
			throw new RuntimeException();
		}
		
		SigninResult signinResult = SigninResult.builder().token(jwtTokenProvider.createToken(String.valueOf(user.getUid()))).build();
		
		setSuccessResult(signinResult, user.getUid());
		
		return signinResult;
	}
	
	
	@Override
	public SignupResult deleteAccount(SigninRequest delAccRequest) {
		
		User user = userRepository.getByUid(delAccRequest.getId());
		
		if(!passwordEncoder.matches(delAccRequest.getPassword(), user.getPassword())) {
			LOGGER.info("[getSignInResult] 틀린 비밀번호");
			throw new RuntimeException();
		}
		
		userRepository.delete(user);
		
		SignupResult signUpResultDto = new SignupResult();
		setSuccessResult(signUpResultDto, new Object());
		
		return null;
	}
	
	@Override
	public SignupResult updateAddress(UpdateRequest updateRequest) {
		
		if(!jwtTokenProvider.validateToken(updateRequest.getToken())) { //유효하지 않은 토큰
			LOGGER.info("[updateAddress] 틀린 비밀번호");
			throw new RuntimeException();
		}
		
		String uid = jwtTokenProvider.getUserIdFromToken(updateRequest.getToken());
		LOGGER.info("[updateAddress] Token에서 uid 추출 완료");
		User newUser = userRepository.getByUid(uid);
		newUser.setAddress(updateRequest.getNewAddress());
		
		User updateUser = userRepository.save(newUser);
		LOGGER.info("[updateAddress] save 메소드까지 완료");
		
		SignupResult signUpResultDto = new SignupResult();
		
		if(!updateUser.getName().isEmpty()) {
			setSuccessResult(signUpResultDto, updateUser);
			LOGGER.info("[UpdateAddress] 정상처리 완료");
		} else {
			setFailResult(signUpResultDto, new Object());
			LOGGER.info("[UpdateAddress] 실패처리 완료");
		}
		return signUpResultDto;
	}

	
	
	
	
	private void setSuccessResult(ApiResponse result, Object obj) {
		LOGGER.info("[setSuccessResult] SuccessResult 생성");
		result.setCode(0);
		result.setMsg("Success");
		result.setData(obj);
	}
	
	private void setFailResult(ApiResponse result, Object obj) {
		result.setCode(-1);
		result.setMsg("Fail");
		result.setData(obj);
	}


	

	
}
