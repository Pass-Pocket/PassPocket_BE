package com.mp.passPocket.flight.service.impl;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
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
import com.mp.passPocket.flight.data.dto.repository.FlightWishListRepository;
import com.mp.passPocket.flight.data.dto.request.SearchConditionRequest;
import com.mp.passPocket.flight.data.dto.response.FlightDetailResponse;
import com.mp.passPocket.flight.data.dto.response.flightOffers.AdditionalInfo;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightInfo;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightItineraryInfo;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightList;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightPriceInfo;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightSearchSegment;
import com.mp.passPocket.flight.data.entity.PortCode;
import com.mp.passPocket.flight.data.entity.WishList;
import com.mp.passPocket.flight.service.FlightService;

@Service
public class FlightServiceImpl implements FlightService {

	private FlightLocationRepository flightLocationRepositroy;
	private FlightAirlineRepository flightAirlineRepositroy;
	private FlightWishListRepository flightWishListRepository;
	private final Logger LOGGER = LoggerFactory.getLogger(FlightServiceImpl.class);
	
	
	private final RedisTemplate<String, Object> redisTemplate;
    private final static long REDIS_TTL = 1;

	
	@Autowired
	public FlightServiceImpl(FlightLocationRepository flightLocationRepositroy, 
			FlightAirlineRepository flightAirlineRepositroy, 
			FlightWishListRepository flightWishListRepository,
			RedisTemplate<String, Object> redisTemplate) {
		super();
		this.flightLocationRepositroy = flightLocationRepositroy;
		this.flightAirlineRepositroy = flightAirlineRepositroy;
		this.flightWishListRepository = flightWishListRepository;
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
					
					//Departure Data
					FlightInfo departure = FlightInfo.builder().iataCode(oriSeg.getDeparture().getIataCode())
							.portName(flightLocationRepositroy.findPNameKorByPCode(oriSeg.getDeparture().getIataCode()))
							.at(oriSeg.getDeparture().getAt())
							.terminal(oriSeg.getDeparture().getTerminal())
							.build();
					
					
					// Arrival Data
					FlightInfo arrival = FlightInfo.builder()
							.iataCode(oriSeg.getArrival().getIataCode())
							.portName(flightLocationRepositroy.findPNameKorByPCode(oriSeg.getArrival().getIataCode()))
							.at(oriSeg.getArrival().getAt())
							.terminal(oriSeg.getArrival().getTerminal())
							.build();
					
					
					// AdditinalInfo Data
					AdditionalInfo addInfo = AdditionalInfo.builder()
							.baggage("무료 위탁 수하물 1개")
							.recommandDepatureTime("07:30")
							.wifi("T")
							.inFightMeal("1회 제공")
							.build();
					
					FlightSearchSegment newSeg = FlightSearchSegment.builder()
							.departure(departure)
							.arrival(arrival)
							.carrierCode(oriSeg.getCarrierCode())
							.carrierNumber(oriSeg.getNumber())
							.carrierName(flightAirlineRepositroy.findAirlineNameByAirlineCode(oriSeg.getCarrierCode()))
							.aircraft(oriSeg.getAircraft().getCode())
							.duration(oriSeg.getDuration())
							.numberOfStops(String.valueOf(oriSeg.getNumberOfStops()))
							.info(addInfo)
							.build();
					

					newIt[s] = newSeg;
				}
				
				
				
				FlightItineraryInfo info = FlightItineraryInfo.builder().transfer(newIt.length-1).segments(newIt).build();
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
	
	
	@Override
	public FlightList[] getFlightFilterResponse(FlightList[] flightOffers,
	                                            SearchConditionRequest searchConditionRequest) {
	    List<String> numOfStopList = Arrays.asList(searchConditionRequest.getNumOfStop());
	    List<String> preferAirlineList = Arrays.asList(searchConditionRequest.getPreferAirline());

	    return Arrays.stream(flightOffers)
	        .filter(flight -> 
	            Arrays.stream(flight.getItineraries())
	                .anyMatch(itinerary -> 
	                    numOfStopList.contains(String.valueOf(itinerary.getTransfer())) &&
	                    Arrays.stream(itinerary.getSegments())
	                        .anyMatch(segment -> preferAirlineList.contains(segment.getCarrierCode()))
	                )
	        )
	        .toArray(FlightList[]::new);
	}


	@Override
	
	public FlightList[] getFlightSortResponse(FlightList[] flightOffers
			, String sort) {
		
		switch(sort) {
		case "LowestPrice" : 
			return Arrays.stream(flightOffers).sorted(Comparator.comparing(item -> item.getPrice().getTotal())).toArray(FlightList[]::new);
			
		case "MinTransfer" : 
			
			return Arrays.stream(flightOffers)
		      .sorted(Comparator.comparing(item -> 
		            Arrays.stream(item.getItineraries())
		                  .mapToInt(ItinerariesItem -> ItinerariesItem.getTransfer())
		                  .sum()
		      ))
		      .toArray(FlightList[]::new);
			
		case "ShortestDuration" : 
			return Arrays.stream(flightOffers)
				    .sorted(Comparator.comparing(item -> 
			        Arrays.stream(item.getItineraries())
			            .flatMap(itinerary -> Arrays.stream(itinerary.getSegments()))
			            .mapToInt(segment -> {
			                Duration duration = Duration.parse(segment.getDuration());
			                return (int) duration.toMinutes();
			            })
			            .sum() 
			    ))
			    .toArray(FlightList[]::new);
		}
		
		return null;
	}
	
	
	// 특정 FlightOffer 데이터
	@Override
	public FlightList getFlightDetail(FlightList[] flightOffers, String flightId) {
		return Arrays.stream(flightOffers)
                .filter(flight -> flightId.equals(flight.getFlightId()))
                .findFirst()
                .orElse(null); // 값이 없을 경우 null 반환
	}
	
	
	
	/*
	 * MongoDB 관련 메소드
	 */
	
	public String saveWishItem(String key, FlightDetailResponse value){
		WishList wishItem = new WishList(key, value);
		WishList result = flightWishListRepository.save(wishItem);
        if(result == null) return "400.ERROR";
        return "200. OK";
	}
	
	@Override
	public List<WishList> getWishList(String userId) {
		return flightWishListRepository.findByKey(userId);
	}

	
	
	
	
	/*
	 * Redis 관련 메소드
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
