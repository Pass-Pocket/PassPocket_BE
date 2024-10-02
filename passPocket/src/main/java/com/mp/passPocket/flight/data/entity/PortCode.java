package com.mp.passPocket.flight.data.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.mp.passPocket.auth.data.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@Table(name = "PORTCODE")
public class PortCode {
	@Id @Column(name = "CODE_NO")
	private long codeNo;
	
	@Column(nullable = false, name = "PNAME_ENG")
	private String PNameEng;

	@Column(nullable = false, name = "PNAME_KOR")
	private String PNameKor;

	@Column(nullable = false, name = "PCODE")
	private String pCode;

	@Column(nullable = false, name = "COUNTRY")
	private String country;

	@Builder
	public PortCode(long codeNo, String PNameEng, String PNameKor, String pCode, String country) {
		super();
		this.codeNo = codeNo;
		this.PNameEng = PNameEng;
		this.PNameKor = PNameKor;
		this.pCode = pCode;
		this.country = country;
	}
		
	
}
