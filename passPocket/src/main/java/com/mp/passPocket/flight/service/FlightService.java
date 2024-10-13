package com.mp.passPocket.flight.service;

import java.util.List;

import com.amadeus.resources.FlightOfferSearch;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightList;
import com.mp.passPocket.flight.data.entity.PortCode;

public interface FlightService {
	
	// Get PortCode
	public List<PortCode> searchLoacation(String keyword);
	
	//
	public FlightList[] getFlightResponse(FlightOfferSearch[] flightOffers);
	
	// 항공편 정보를 Redis에 저장하는 메소드
	public void saveFlightOffers(String flightKey, FlightList[] flightOffers);

    // 항공편 정보를 Redis에서 조회하는 메소드
    public FlightList[] getFlightOffers(String flightKey);

}
