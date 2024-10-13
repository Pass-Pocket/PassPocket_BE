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
@Builder
public class SearchFlightRoundRequest extends FlightSearchSegment {

	private String returnDate;
	private String adults;
	private String currencyCode = "KRW";

}
