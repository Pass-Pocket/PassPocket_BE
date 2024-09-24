package com.mp.passPocket.auth.data.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
@AllArgsConstructor
@ToString
@Builder
@Table(name = "MEM")
public class User implements UserDetails  {
	
	@Id @Column(name = "USER_NO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
	private long id;
	
	@Column(nullable = false, unique = true, name = "user_id")
	private String uid;

	@Column(nullable = false, name = "USER_PWD")
	private String password;

	@Column(nullable = false, name = "USER_NAME")
	private String name;

	@Column(nullable = false, name = "PHONE")
	private String phone;

	@Column(nullable = false, name = "ADDRESS")
	private String address;
	
	
	
	@Builder
	public User(String uid, String password, String name, String phone, String address) {
		super();
		this.uid = uid;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.address = address;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.uid;
	}

	

	
}
