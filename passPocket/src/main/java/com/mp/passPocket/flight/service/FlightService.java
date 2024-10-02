package com.mp.passPocket.flight.service;

import java.util.List;

import com.mp.passPocket.flight.data.dto.request.PortCodeRequest;
import com.mp.passPocket.flight.data.entity.PortCode;

public interface FlightService {
	
	public List<PortCode> searchLoacation(String keyword);

}
