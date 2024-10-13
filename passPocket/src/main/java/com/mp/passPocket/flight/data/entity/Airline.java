package com.mp.passPocket.flight.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@Table(name = "AIRLINE")
public class Airline {
	@Id @Column(name = "AIRLINE_CODE")
	private String airlineCode;
	
	@Column(nullable = false, name = "AIRLINE_NAME")
	private String airlineName;
	
	@Builder
	public Airline(String airlineCode, String airlineName) {
		this.airlineCode = airlineCode;
		this.airlineName = airlineName;
	}

}
