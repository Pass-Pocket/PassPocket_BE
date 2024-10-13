package com.mp.passPocket.flight.data.dto.response.flightOffers;


import com.amadeus.resources.FlightOfferSearch.Itinerary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightList {
	
	private String FlightId;			// 순번
	private String numberOfBookSeats;	// 잔여 좌석
	private FlightPriceInfo price; 		// 항공기 가격 정보
	private FlightItineraryInfo[] itineraries; 	// 항공편 정보(  (출발지, 터미널, 출발시간)  (도착지, 터미널, 도착시간)  )
	
	
	

}
