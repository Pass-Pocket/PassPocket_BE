package com.mp.passPocket.flight.data.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchConditionRequest {

	private String redisKey;
	private String sortCondition = "recommand";   // 정렬 조건 Recommand , LowestPrice , MinTransfer, ShortestDuration
	private String[] numOfStop;		// 경유 횟수
	private String[] preferAirline;	// 항공사
	
	//출도착 필터는 정렬에 들어가야 하는거 아닌가..?
		
}
