package com.mp.passPocket.flight.data.dto.response;

import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightDetailResponse {
	
	private String redisKey;
	private String flightId;
	private FlightList flight;
	
}
