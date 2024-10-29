package com.mp.passPocket.flight.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.mp.passPocket.common.connect.sdk.AmadeusConnect;
import com.mp.passPocket.flight.data.dto.request.FlightDetailRequest;
import com.mp.passPocket.flight.data.dto.request.SaveWishListRequest;
import com.mp.passPocket.flight.data.dto.request.SearchConditionRequest;
import com.mp.passPocket.flight.data.dto.request.SearchFlightMultiRequest;
import com.mp.passPocket.flight.data.dto.request.SearchFlightRequest;
import com.mp.passPocket.flight.data.dto.request.SearchFlightRoundRequest;
import com.mp.passPocket.flight.data.dto.response.FlightDetailResponse;
import com.mp.passPocket.flight.data.dto.response.FlightListResponse;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightList;
import com.mp.passPocket.flight.data.entity.PortCode;
import com.mp.passPocket.flight.data.entity.WishList;
import com.mp.passPocket.flight.service.FlightService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;



@RestController
@RequestMapping("/flight")
public class FlightController {

	
	private final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);
	private final FlightService flightService;
	private final AmadeusConnect amaduesConnect;


	@Autowired
	public FlightController(FlightService flightService, AmadeusConnect amaduesConnect) {
		super();
		this.flightService = flightService;
		this.amaduesConnect = amaduesConnect;
	}
	
	@Operation(summary = "GetLocation", description = "공항/도시 코드 조회")
	@GetMapping("get-location")
    public List<PortCode> locations(
    		@Parameter(description = "getLocationCode Keyword", required = true)
    		@RequestParam(required=true) String keyword) throws ResponseException {
		LOGGER.info("[FlightController] - 항공코드 요청");

		List<PortCode> location = flightService.searchLoacation(keyword);
		// 15개만 넘기기
        return location;
    }
	
	@Operation(summary = "GetFlightList", description = "항공편 목록 조회 - 편도")
	@PostMapping("get-flightlist/oneWay")
    public FlightListResponse searchFlightOneway(
    		@Parameter(description = "searchFlight request details", required = true)
	        @RequestBody SearchFlightRequest searchFlightRequest
    		) throws ResponseException {
		
		FlightListResponse response;
		
		LOGGER.info("[FlightController] - 편도 항공편 목록 조회 요청");
		
		String[] data = {searchFlightRequest.getOriginLocationCode(),
				searchFlightRequest.getDestinationLocationCode(),
				searchFlightRequest.getDepartureDate()};

		
		String flightKey = generateFlightKey(data,"oneWay");
		
		FlightList[] cachedFlightOffers = flightService.getFlightOffers(flightKey);
		LOGGER.info("[FlightController] - 편도 항공편 목록 캐시 데이터 조회");


        // 캐시에 정보가 없으면 API 호출하여 데이터 가져오기
        if (cachedFlightOffers == null) {
        	FlightOfferSearch[] flightOffers = amaduesConnect.flights(searchFlightRequest);
    		LOGGER.info("[FlightController] - 편도 항공편 목록 API 호출 완료");
    		
    		FlightList[] offersResponse = flightService.getFlightResponse(flightOffers);
    		LOGGER.info("[FlightController] - 편도 항공편 사용 데이터 추출 완료");
    		
            // 가져온 항공편 정보를 Redis에 저장
            flightService.saveFlightOffers(flightKey, offersResponse);
    		LOGGER.info("[FlightController] - 편도 항공편 목록 API Redis 저장 완료");
    		
    		response = FlightListResponse.builder().list(offersResponse).redisKey(flightKey).build();

        } else {
    		LOGGER.info("[FlightController] - 편도 항공편 목록 캐시 데이터 조회 완료");
    		
    		response = FlightListResponse.builder().list(cachedFlightOffers).redisKey(flightKey).build();        	
        }

        // 캐시에 데이터가 있으면 그것을 반환
        return response;
    }
	
	
	
	
	@Operation(summary = "GetFlightList", description = "항공편 목록 조회 - 왕복")
	@PostMapping("get-flightlist/round")
    public FlightListResponse searchFlightRound(
    		@Parameter(description = "searchFlight request details", required = true)
	        @RequestBody SearchFlightRoundRequest searchFlightRoundRequest
	       ) throws ResponseException {
		FlightListResponse response;
		
		LOGGER.info("[FlightController] - 왕복 항공편 목록 조회 요청");
		
		String[] data = {searchFlightRoundRequest.getOriginLocationCode(),
				searchFlightRoundRequest.getDestinationLocationCode(),
				searchFlightRoundRequest.getDepartureDate()};

		
		String flightKey = generateFlightKey(data,"ROUND");
		
		FlightList[] cachedFlightOffers = flightService.getFlightOffers(flightKey);
		LOGGER.info("[FlightController] - 왕복 항공편 목록 캐시 데이터 조회");


        // 캐시에 정보가 없으면 API 호출하여 데이터 가져오기
        if (cachedFlightOffers == null) {
        	FlightOfferSearch[] flightOffers = amaduesConnect.flights(searchFlightRoundRequest);
    		LOGGER.info("[FlightController] - 왕복 항공편 목록 API 호출 완료");
    		
    		FlightList[] offersResponse = flightService.getFlightResponse(flightOffers);
    		LOGGER.info("[FlightController] - 왕복 항공편 사용 데이터 추출 완료");
    		
            // 가져온 항공편 정보를 Redis에 저장
            flightService.saveFlightOffers(flightKey, offersResponse);
    		LOGGER.info("[FlightController] - 왕복 항공편 목록 API Redis 저장 완료");
    		
    		response = FlightListResponse.builder().list(offersResponse).redisKey(flightKey).build();

        } else {
    		LOGGER.info("[FlightController] - 편도 항공편 목록 캐시 데이터 조회 완료");
    		response = FlightListResponse.builder().list(cachedFlightOffers).redisKey(flightKey).build();        	
        }

        // 캐시에 데이터가 있으면 그것을 반환
        return response;
    }
	
	
	@Operation(summary = "GetFlightList", description = "항공편 목록 조회 - 다구간")
	@PostMapping("get-flightlist/multi")
    public FlightList[] searchFlightMulti(
    		@Parameter(description = "searchFlight request details", required = true)
	        @RequestBody SearchFlightMultiRequest searchFlightMultiRequest
	       ) throws ResponseException {
		LOGGER.info("[FlightController] - 다구간 항공편 목록 조회 요청");
		
//		searchFlightMultiRequest.getList().stream().forEach()
		

    	FlightOfferSearch[] flightOffers = amaduesConnect.flights(searchFlightMultiRequest);
		LOGGER.info("[FlightController] - 다구간 항공편 목록 API 호출 완료");
		
		FlightList[] offersResponse = flightService.getFlightResponse(flightOffers);
        return offersResponse;
	}
	
	
	@Operation(summary = "GetFlightList", description = "항공편 목록 조회 - 다구간")
	@PostMapping("get-flightlist/condition")
    public FlightList[] searchConditionReqeust(
	        @Parameter(description = "Condition", required = true)
	        @RequestBody SearchConditionRequest searchConditionRequest
	       ) throws ResponseException {
		
		FlightList[] cachedFlightOffers = flightService.getFlightOffers(searchConditionRequest.getRedisKey());
		LOGGER.info("[FlightController] - 정렬 및 필터 항공편 목록 캐시 데이터 조회 : " + cachedFlightOffers.length);
		
		
		FlightList[] filterList = flightService.getFlightFilterResponse(cachedFlightOffers, searchConditionRequest);
		
		String sortCondition = searchConditionRequest.getSortCondition();
		//정렬 기준 적용
		if(!sortCondition.equals("recommand")) {
			FlightList[] offersResponse = flightService.getFlightSortResponse(filterList, sortCondition);
			LOGGER.info("[FlightController] - 항공편 필터 및 정렬 적용 완료 : " + offersResponse.length);
			return offersResponse;
		}


		LOGGER.info("[FlightController] - 항공편 필터 적용 완료 : " + filterList.length);
		
		return filterList;
	}
	
	
	
	
	
	@Operation(summary = "GetFlightDetail", description = "항공편 상세 조회")
	@PostMapping("get-flightDetail")
    public FlightDetailResponse getFlightDetail(
    		@Parameter(description = "DetailResponse", required = true)
	        @RequestBody FlightDetailRequest flightDetailRequest
	       ) throws ResponseException {
		
		FlightList[] cachedFlightOffers = flightService.getFlightOffers(flightDetailRequest.getRedisKey());
		
		return FlightDetailResponse.builder()
				.redisKey(flightDetailRequest.getRedisKey())
				.flight(flightService.getFlightDetail(cachedFlightOffers, flightDetailRequest.getFlightId()))
				.build();
	}
	
	@Operation(summary = "SaveWishList", description = "위시리스트")
	@PostMapping("save-wishList")
    public String saveWishList(
    		@Parameter(description = "DetailResponse", required = true)
	        @RequestBody SaveWishListRequest saveWishListRequest
	       ) throws ResponseException {
		
		FlightList[] cachedFlightOffers = flightService.getFlightOffers(saveWishListRequest.getRedisKey());
		
		FlightDetailResponse flight =  FlightDetailResponse.builder()
				.redisKey(saveWishListRequest.getRedisKey())
				.flight(flightService.getFlightDetail(cachedFlightOffers, saveWishListRequest.getFlightId()))
				.build();
		return flightService.saveWishItem(saveWishListRequest.getUserId(), flight);
	}
	
	@Operation(summary = "GetWishList", description = "위시리스트 조회")
	@GetMapping("get-wishList/{userId}")
	public List<WishList> getWishList(@PathVariable String userId) {
	    return flightService.getWishList(userId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//redis
	private String generateFlightKey(String[] data, String classification ) {
		StringBuilder keyBuilder = new StringBuilder();
		
		for(String s : data) {
			keyBuilder.append(s);
		}
		
		keyBuilder.append(classification);
		
		return keyBuilder.toString();
		
	}
	
	
	
	
	
}
