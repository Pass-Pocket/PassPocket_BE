package com.mp.passPocket.flight.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOfferSearch.SearchSegment;
import com.mp.passPocket.flight.data.dto.repository.FlightAirlineRepository;
import com.mp.passPocket.flight.data.dto.repository.FlightLocationRepository;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightInfo;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightItineraryInfo;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightList;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightPriceInfo;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightSearchSegment;
import com.mp.passPocket.flight.data.entity.PortCode;
import com.mp.passPocket.flight.service.FlightService;

@Service
public class FlightServiceImpl implements FlightService {

	private FlightLocationRepository flightLocationRepositroy;
	private FlightAirlineRepository flightAirlineRepositroy;
	private final Logger LOGGER = LoggerFactory.getLogger(FlightServiceImpl.class);
	
	
	private final RedisTemplate<String, Object> redisTemplate;
    private final static long REDIS_TTL = 1;

	
	@Autowired
	public FlightServiceImpl(FlightLocationRepository flightLocationRepositroy, 
			FlightAirlineRepository flightAirlineRepositroy, 
			RedisTemplate<String, Object> redisTemplate) {
		super();
		this.flightLocationRepositroy = flightLocationRepositroy;
		this.flightAirlineRepositroy = flightAirlineRepositroy;
		this.redisTemplate = redisTemplate;
	}
	
	

	@Override
	public List<PortCode> searchLoacation(String keyword) {
		LOGGER.info("[searchLoacation] 항공코드 조회 키워드 전달");
		
		List<PortCode> location = flightLocationRepositroy.searchWithRowNum(keyword, keyword, keyword, 15);
		return location;
	}
	
	
	
	
	@Override
	public FlightList[] getFlightResponse(FlightOfferSearch[] flightOffers) {
		
		int n = flightOffers.length;
		FlightList[] list = new FlightList[n];
		
		for(int i = 0 ; i < n ; i++ ) { //offer 배열생성
			
			FlightOfferSearch offer = flightOffers[i];
			
			
			FlightPriceInfo price = FlightPriceInfo.builder().currency(offer.getPrice().getCurrency())
					.base(offer.getPrice().getBase())
					.total(offer.getPrice().getTotal()).build();
			
			FlightItineraryInfo[] itineraries = new FlightItineraryInfo[offer.getItineraries().length];
			
			
			
			for(int j = 0 ; j < offer.getItineraries().length; j++) { //여정 배열 생성

				SearchSegment[] oriIt = ((offer.getItineraries())[j]).getSegments();
				FlightSearchSegment[] newIt = new FlightSearchSegment[oriIt.length];
				
				
				for(int s = 0 ; s < oriIt.length ; s++) { //Segment 배열 생성
					
					SearchSegment oriSeg = oriIt[s];
					
					FlightInfo departure = FlightInfo.builder().iataCode(oriSeg.getDeparture().getIataCode())
							.at(oriSeg.getDeparture().getAt())
							.terminal(oriSeg.getDeparture().getTerminal())
							.build();
					
					FlightInfo arrival = FlightInfo.builder()
							.iataCode(oriSeg.getArrival().getIataCode())
							.at(oriSeg.getArrival().getAt())
							.terminal(oriSeg.getArrival().getTerminal())
							.build();
					
					FlightSearchSegment newSeg = FlightSearchSegment.builder()
							.departure(departure)
							.arrival(arrival)
							.carrierCode(oriSeg.getCarrierCode() + oriSeg.getNumber())
							.carrierName(flightAirlineRepositroy.findAirlineNameByAirlineCode(oriSeg.getCarrierCode()))
							.aircraft(oriSeg.getAircraft().getCode())
							.duration(oriSeg.getDuration())
							.numberOfStops(String.valueOf(oriSeg.getNumberOfStops()))
							.build();
					

					newIt[s] = newSeg;
				}
				
				
				
				FlightItineraryInfo info = FlightItineraryInfo.builder().segments(newIt).build();
				itineraries[j] = info;
			}
			
			
			
			
			
			FlightList response = FlightList.builder()
					.FlightId(offer.getId())
					.numberOfBookSeats(String.valueOf(offer.getNumberOfBookableSeats()))
					.price(price)
					.itineraries(itineraries)
					.build();
			
			list[i] = response;
		}

		
		return list;
	}
	
	
	


	/*
	 * Redis 관련
	 */
	
	// 항공편 정보를 Redis에 저장하는 메소드
    public void saveFlightOffers(String flightKey, FlightList[] flightOffers) {
        redisTemplate.opsForValue().set(flightKey, flightOffers, REDIS_TTL, TimeUnit.HOURS);
    }

    // 항공편 정보를 Redis에서 조회하는 메소드
    public FlightList[] getFlightOffers(String flightKey) {
        return (FlightList[]) redisTemplate.opsForValue().get(flightKey);
    }




	
}
