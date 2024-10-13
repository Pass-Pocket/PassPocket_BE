package com.mp.passPocket.flight.data.dto.response.flightOffers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightPriceInfo {
	private String currency;
    private double total;
    private double base;

}