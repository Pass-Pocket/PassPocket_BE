package com.mp.passPocket.flight.data.dto.response.flightOffers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfo {

	private String baggage;
	private String recommandDepatureTime;
	private String inFightMeal;
	private String wifi;
	
}
