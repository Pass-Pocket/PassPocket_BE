package com.mp.passPocket.flight.data.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightSearchRequest {
	private String originLocationCode;
	private String destinationLocationCode;
	private String departureDate;
	private String adults;
	private String currencyCode = "KRW";

}
