package com.mp.passPocket.flight.data.dto.response.flightOffers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightItineraryInfo {
	
	private int transfer; // 환승 수
	private FlightSearchSegment[] segments;

}