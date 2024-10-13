package com.mp.passPocket.flight.data.dto.response;

import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightItineraryInfo;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightList;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightPriceInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightListResponse {

	private String redisKey;
	private FlightList[] list;
}
