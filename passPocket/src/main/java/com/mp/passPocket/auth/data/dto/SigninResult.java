package com.mp.passPocket.auth.data.dto;

import com.mp.passPocket.common.dto.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SigninResult extends ApiResponse {
	
	private String token;
	
	@Builder
	public SigninResult(int code, String msg, Object obj, String token) {
		super(code, msg, obj);
		this.token = token;

	}
	
}