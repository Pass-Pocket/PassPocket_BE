package com.mp.passPocket.common.connect.sdk;

import org.springframework.beans.factory.annotation.Value;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;

public enum AmadeusConnect {
    INSTANCE;
	
	@Value("${amadeus.api.key}")
	private String amadeusKey;
	
	@Value("${amadeus.api.secret}")
	private String amadeusSecret ;
	
    private Amadeus amadeus;
    private AmadeusConnect() {
        this.amadeus = Amadeus
            .builder(amadeusKey, amadeusSecret)
            .build();
    }
    public Location[] location(String keyword) throws ResponseException {
        return amadeus.referenceData.locations.get(Params
            .with("keyword", keyword)
            .and("subType", Locations.AIRPORT));
    }
}