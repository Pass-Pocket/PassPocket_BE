package com.mp.passPocket.flight.data.dto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mp.passPocket.flight.data.entity.PortCode;

public interface FlightLocationRepository extends JpaRepository<PortCode, Integer>  {
	
	@Query("SELECT pc FROM PortCode pc WHERE (pc.PNameEng LIKE CONCAT('%', :PNameEng, '%') OR pc.PNameKor LIKE CONCAT('%', :PNameKor, '%') OR pc.country LIKE CONCAT('%', :country, '%')) AND ROWNUM <= :limit")
	List<PortCode> searchWithRowNum(@Param("PNameEng") String PNameEng, @Param("PNameKor") String PNameKor, @Param("country") String country, @Param("limit") int limit);

	@Query("SELECT pc.PNameKor FROM PortCode pc WHERE pc.pCode = :pCode")
	String findPNameKorByPCode(@Param("pCode") String pCode);

}
