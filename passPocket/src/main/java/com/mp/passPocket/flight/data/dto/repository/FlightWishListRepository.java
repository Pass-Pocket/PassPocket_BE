package com.mp.passPocket.flight.data.dto.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mp.passPocket.flight.data.entity.WishList;

public interface FlightWishListRepository extends MongoRepository<WishList, String> {

	
	List<WishList> findByKey(String userId);
}
