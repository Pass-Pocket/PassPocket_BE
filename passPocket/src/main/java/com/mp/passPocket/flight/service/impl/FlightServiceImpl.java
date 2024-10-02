package com.mp.passPocket.flight.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mp.passPocket.flight.data.dto.repository.FlightLocationRepository;
import com.mp.passPocket.flight.data.entity.PortCode;
import com.mp.passPocket.flight.service.FlightService;

@Service
public class FlightServiceImpl implements FlightService {

	private FlightLocationRepository flightLocationRepositroy;
	private final Logger LOGGER = LoggerFactory.getLogger(FlightServiceImpl.class);

	
	@Autowired
	public FlightServiceImpl(FlightLocationRepository flightLocationRepositroy) {
		super();
		this.flightLocationRepositroy = flightLocationRepositroy;
	}
	
	

	@Override
	public List<PortCode> searchLoacation(String keyword) {
		LOGGER.info("[searchLoacation] 회원 가입 정보 전달");

		List<PortCode> location = flightLocationRepositroy.findByPNameEngContainingOrPNameKorContaining(keyword, keyword);
		
		
		return location;
	}
	
	
}
