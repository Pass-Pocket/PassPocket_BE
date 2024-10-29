package com.mp.passPocket.flight.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mp.passPocket.flight.data.dto.response.FlightDetailResponse;
import com.mp.passPocket.flight.data.dto.response.flightOffers.FlightList;

import lombok.Data;

@Document(collection = "WishList")
@Data
public class WishList {
	
	
	@Id
    private String id; // MongoDB의 기본 키
    private String key; // 특정 키 값
    private FlightDetailResponse value; // JSON 형식의 값
    
    public WishList(String key , FlightDetailResponse value) {
    	this.key = key;
    	this.value = value;
    }
    
}
