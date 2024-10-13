package com.mp.passPocket.flight.data.dto.request;

import java.util.List;

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
@Builder
public class SearchFlightMultiRequest {
	
	private List<FlightSearchSegment> list;
	private String adults;
	private String currencyCode = "KRW";


}
