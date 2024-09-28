package com.mp.passPocket.flight.data.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PortCodeRequest {

	String subType = "any";
	String keyword;
	int limit = 10;

}
