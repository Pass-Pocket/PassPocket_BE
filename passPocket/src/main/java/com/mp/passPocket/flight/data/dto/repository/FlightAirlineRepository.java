package com.mp.passPocket.flight.data.dto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mp.passPocket.flight.data.entity.Airline;
import com.mp.passPocket.flight.data.entity.PortCode;

public interface FlightAirlineRepository extends JpaRepository<Airline, String>  {
	
	// 특정 필드만 반환하도록 @Query 어노테이션을 추가
    @Query("SELECT a.airlineName FROM Airline a WHERE a.airlineCode = :airlineCode")
    String findAirlineNameByAirlineCode(@Param("airlineCode") String airlineCode);
    
    
}