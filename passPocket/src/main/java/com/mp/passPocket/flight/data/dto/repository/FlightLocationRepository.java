package com.mp.passPocket.flight.data.dto.repository;

import java.util.List;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mp.passPocket.flight.data.entity.PortCode;

public interface FlightLocationRepository extends JpaRepository<PortCode, Integer>  {
	
	List<PortCode> findByPNameEngContainingOrPNameKorContaining(String nameEng, String nameKor);
}
