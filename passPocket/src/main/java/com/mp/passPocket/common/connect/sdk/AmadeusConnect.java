package com.mp.passPocket.common.connect.sdk;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mp.passPocket.flight.data.dto.request.FlightSearchSegment;
import com.mp.passPocket.flight.data.dto.request.SearchFlightMultiRequest;
import com.mp.passPocket.flight.data.dto.request.SearchFlightRequest;
import com.mp.passPocket.flight.data.dto.request.SearchFlightRoundRequest;

import jakarta.annotation.PostConstruct;


@Component
public class AmadeusConnect {
	
	private final Logger LOGGER = LoggerFactory.getLogger(AmadeusConnect.class);

	@Value("${amadeus.api.key}")
	private String amadeusKey;
	
	@Value("${amadeus.api.secret}")
	private String amadeusSecret ;
	
    private Amadeus amadeus;
    
    @PostConstruct
    private void init() {
        this.amadeus = Amadeus
            .builder(amadeusKey, amadeusSecret)
            .build();
    }
    
    public Location[] location(String keyword) throws ResponseException {
        return amadeus.referenceData.locations.get(Params
            .with("keyword", keyword)
            .and("subType", Locations.AIRPORT));
    }
    
    /**
     * 
     * @param searchFlightRequest
     * @return
     * @throws ResponseException
     */
    public FlightOfferSearch[] flights(SearchFlightRequest searchFlightRequest) throws ResponseException {
    	
    	LOGGER.info("[AmadeusConnect] - 편도 목록조회 호출");
    	
    	
        return amadeus.shopping.flightOffersSearch.get(
                  Params.with("originLocationCode", searchFlightRequest.getOriginLocationCode())
                          .and("destinationLocationCode", searchFlightRequest.getDestinationLocationCode())
                          .and("departureDate", searchFlightRequest.getDepartureDate())
                          .and("adults", searchFlightRequest.getAdults())
                          .and("currencyCode", searchFlightRequest.getCurrencyCode())
                          .and("max", 5));
    }
    
    
    /**
     * 
     * @param searchFlightRoundRequest
     * @return
     * @throws ResponseException
     */
    
    
    
    public FlightOfferSearch[] flights(SearchFlightRoundRequest searchFlightRoundRequest) throws ResponseException {
		LOGGER.info("[AmadeusConnect] - 왕복 목록조회 호출");
    	
        return amadeus.shopping.flightOffersSearch.get(
                  Params.with("originLocationCode", searchFlightRoundRequest.getOriginLocationCode())
                          .and("destinationLocationCode", searchFlightRoundRequest.getDestinationLocationCode())
                          .and("departureDate", searchFlightRoundRequest.getDepartureDate())
                          .and("returnDate", searchFlightRoundRequest.getReturnDate())
                          .and("adults", searchFlightRoundRequest.getAdults())
                          .and("currencyCode", searchFlightRoundRequest.getCurrencyCode())
                          .and("max", 5));
    }
    
    /**
     * 
     * @param searchFlightMultiRequest
     * @return
     * @throws ResponseException
     */
    
    public FlightOfferSearch[] flights(SearchFlightMultiRequest searchFlightMultiRequest) throws ResponseException {
    	LOGGER.info("[AmadeusConnect] - 다구간 목록조회 호출");
    	
    	JsonObject request = new JsonObject();
    	
    	List<FlightSearchSegment> list = searchFlightMultiRequest.getList();
    	
    	// 통화코드 생성
    	request.addProperty("currencyCode", searchFlightMultiRequest.getCurrencyCode());
    	
    	// 목적지 리스트 배열 생성
    	JsonArray originDestinations = new JsonArray();
    	for(int i = 0 ; i < list.size() ; i++) {
    		FlightSearchSegment seg = list.get(i);
    		JsonObject segment = new JsonObject();
    		segment.addProperty("id", String.valueOf(i+1));
    		segment.addProperty("originLocationCode", seg.getOriginLocationCode());
    		segment.addProperty("destinationLocationCode", seg.getDestinationLocationCode());
            JsonObject departure = new JsonObject();
            departure.addProperty("date", seg.getDepartureDate());
            segment.add("departureDateTimeRange", departure);
            originDestinations.add(segment);
    	}
    	
    	// travelers 배열 생성
        JsonArray travelers = new JsonArray();
        JsonObject traveler = new JsonObject();
        traveler.addProperty("id", searchFlightMultiRequest.getAdults());
        traveler.addProperty("travelerType", "ADULT");
        JsonArray fareOptions = new JsonArray();
        fareOptions.add("STANDARD");
        traveler.add("fareOptions", fareOptions);
        travelers.add(traveler);
    	
    	JsonArray sources = new JsonArray();
        sources.add("GDS");
        
        JsonObject searchCriteria = new JsonObject();
        searchCriteria.addProperty("maxFlightOffers", 10);
        
        request.add("originDestinations", originDestinations);
        request.add("travelers", travelers);
        request.add("sources", sources);
        request.add("searchCriteria", searchCriteria);

    	
    	
    	// Amadeus API 호출
        FlightOfferSearch[] flightOffers = 	amadeus.shopping.flightOffersSearch.post(request);

        return flightOffers;
    }
}