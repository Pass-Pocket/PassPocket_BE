package com.mp.passPocket.flight.data.dto;

import com.mp.passPocket.auth.data.dto.SigninRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
	private int no;
	private String pNameEng;
	private String pNameKor;
	private String pCode;
	private String country;
}
