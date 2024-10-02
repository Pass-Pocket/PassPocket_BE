package com.mp.passPocket.flight.data.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightListResponse {
	
	private String originTime;
	private String destinationTime;
	private String originPCode;
	private String destinationPCode;
	private String price;
	

}
