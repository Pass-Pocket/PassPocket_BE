package com.mp.passPocket.flight.data.dto.response.flightOffers;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchSegment {
	
	private FlightInfo departure;
	private FlightInfo arrival;
	private String carrierCode;
	private String carrierNumber;
	private String carrierName;
	private String aircraft;
	private String duration;
	private String numberOfStops;

}