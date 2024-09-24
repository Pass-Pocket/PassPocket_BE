package com.mp.passPocket.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mp.passPocket.auth.data.dto.SigninRequest;
import com.mp.passPocket.auth.data.dto.SigninResult;
import com.mp.passPocket.auth.data.dto.SignupRequest;
import com.mp.passPocket.auth.data.dto.SignupResult;
import com.mp.passPocket.auth.data.dto.UpdateRequest;
import com.mp.passPocket.auth.service.SignService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	private final SignService signService;


	@Autowired
	public AuthController(SignService signsService) {
		super();
		this.signService = signsService;
	}
	
	
	
	@Operation(summary = "SignUp", description = "회원가입")
	@PostMapping("/signUp")
	public SignupResult signUp(
			@Parameter(description = "Signup request details", required = true)
	        @RequestBody SignupRequest signupRequest) {
		LOGGER.info("[AuthController] - 회원가입 요청");
		
		SignupResult signUpResultDto = signService.signUp(signupRequest);
		LOGGER.info("[AuthController] - DB User 저장 완료");
		return signUpResultDto;
		
	}
	
	
	@Operation(summary = "SignIn", description = "로그인")
	@PostMapping(value = "/sign-in")
	public SigninResult signIn(
			@Parameter(description = "SignIn request details", required = true)
	        @RequestBody SigninRequest signinRequest) throws RuntimeException {
		
		LOGGER.info("[AuthController] - 로그인 요청");
			
		SigninResult signinResult = signService.signIn(signinRequest);
		
		if(signinResult.getCode() == 0) {
			LOGGER.info("[signIn] 정상적으로 로그인 되었습니다. id : {} , token : {}", signinRequest.getId(), signinResult.getToken());
		}
		
		
		return signinResult;
		
	}
	
	@Operation(summary = "DeleteAccount", description = "회원 탈퇴")
	@DeleteMapping(value = "/delete-account")
	public SignupResult deleteAccount(
			@Parameter(description = "deleteAccount request details", required = true)
	        @RequestBody SigninRequest delAccRequest
			) {
		LOGGER.info("[AuthController] - 회원 탈퇴 요청");
		
		SignupResult signUpResultDto = signService.deleteAccount(delAccRequest);
		LOGGER.info("[AuthController] - DB User 삭제 완료");
		return signUpResultDto;
	}
	
	
	@Operation(summary = "UpdateAddress", description = "출발지 업데이트")
	@PutMapping(value = "/update-Address")
	public SignupResult updateAddress(
			@Parameter(description = "deleteAccount request details", required = true)
	        @RequestBody UpdateRequest updateRequest
			) {
		
		LOGGER.info("[AuthController] - 회원 주소 정보 변경 요청");
		

		SignupResult signUpResultDto = signService.updateAddress(updateRequest);
		LOGGER.info("[AuthController] - DB User 주소 정보 변경 완료");
		
		return signUpResultDto;
		
	}
	
	
	
	
	
	
	
	
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
		HttpHeaders responseHeaders = new HttpHeaders();
		
		LOGGER.info("[ExceptionHandler] : " + e.getMessage());
		
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		
		Map<String, String> map = new HashMap<>();
		map.put("error type", httpStatus.getReasonPhrase());
		map.put("code", "400");
		map.put("message", "에러 에러");
		
		return new ResponseEntity<>(map, responseHeaders, httpStatus);
	}

	
}
