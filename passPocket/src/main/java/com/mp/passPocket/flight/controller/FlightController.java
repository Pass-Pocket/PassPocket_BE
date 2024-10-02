package com.mp.passPocket.flight.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amadeus.exceptions.ResponseException;
import com.mp.passPocket.auth.controller.AuthController;
import com.mp.passPocket.flight.data.entity.PortCode;
import com.mp.passPocket.flight.service.FlightService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;



@RestController
@RequestMapping("/flight")
public class FlightController {

	
	private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	private final FlightService flightService;


	@Autowired
	public FlightController(FlightService flightService) {
		super();
		this.flightService = flightService;
	}
	
	@Operation(summary = "GetLocation", description = "공항/도시 코드 조회")
	@GetMapping("get-location")
    public List<PortCode> locations(
    		@Parameter(description = "getLocationCode Keyword", required = true)
    		@RequestParam(required=true) String keyword) throws ResponseException {
		
		List<PortCode> location = flightService.searchLoacation(keyword);
		// 15개만 넘기기
        return location;
    }
}
