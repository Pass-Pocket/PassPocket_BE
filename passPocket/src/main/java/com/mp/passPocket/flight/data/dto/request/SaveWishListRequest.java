package com.mp.passPocket.flight.data.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveWishListRequest extends FlightDetailRequest {

	private String userId;
	
}
