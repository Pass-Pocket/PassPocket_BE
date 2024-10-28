package com.mp.passPocket.flight.data.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightDetailRequest {
	private String redisKey;
	private String flightId;
}
