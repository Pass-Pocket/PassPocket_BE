package com.mp.passPocket.auth.data.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mp.passPocket.common.dto.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class SignupResult extends ApiResponse {
	
	@Builder
	public SignupResult(int code, String msg, Object obj) {
		super(code, msg, obj);

	}
	
}
