package com.mp.passPocket.flight.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class FlightSearchSegment  {
	
	private String originLocationCode;
	private String destinationLocationCode;
	private String departureDate;
	
}
