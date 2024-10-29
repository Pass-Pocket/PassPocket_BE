package com.mp.passPocket.flight.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDetailRequest {
	private String redisKey;
	private String flightId;
}
