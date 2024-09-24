package com.mp.passPocket.config.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	
	private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
//	private final UserDetailsService usesrDetailsService;
	
	@Value("${springboot.jwt.secret}")
	private String secretKey = "secretKey";
	private final long tokenValidMillisecond = 1000L*60*60;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
	}
	
	
	/**
	 * CreateToken - 토큰 생성 메소드	
	 * @param userId
	 * @return
	 */
	public String createToken(String userId) {
	    LOGGER.info("[JwtTokenProvider] 토큰 생성 메소드 호출");
	    
	    try {
	        Claims claims = Jwts.claims().setSubject(userId);

	        Date now = new Date();
	        LOGGER.info("[JwtTokenProvider] 토큰 생성 시작");
        	SecretKey secretKey = Keys.hmacShaKeyFor(this.secretKey.getBytes(StandardCharsets.UTF_8)); // SecretKey 변환
//	        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Create a secure key

	        String token = Jwts.builder()
	                .setClaims(claims)
	                .setIssuedAt(now)
	                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
	                .signWith(secretKey) // Use the secure key for signing
	                .compact();

	        LOGGER.info("[JwtTokenProvider] 토큰 생성 완료");
	        return token;
	        
	    } catch (Exception e) {
	        LOGGER.error("[JwtTokenProvider] 토큰 생성 중 오류 발생", e);
	        throw new RuntimeException("토큰 생성 중 오류가 발생했습니다", e);
	    }
	}
	
	public boolean validateToken(String token) {
        try {
        	SecretKey secretKey = Keys.hmacShaKeyFor(this.secretKey.getBytes(StandardCharsets.UTF_8)); // SecretKey 변환
        	Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            LOGGER.info("잘못된 JWT 서명입니다." + e.getMessage());
        } catch (ExpiredJwtException e) {
        	LOGGER.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
        	LOGGER.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
        	LOGGER.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
	
	public String getUserIdFromToken(String token) {
	    try {
	        SecretKey secretKey = Keys.hmacShaKeyFor(this.secretKey.getBytes(StandardCharsets.UTF_8)); // SecretKey 변환

	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(secretKey) // 검증에 사용할 SecretKey 설정
	                .build()
	                .parseClaimsJws(token) // 토큰 파싱
	                .getBody(); // Claims 객체 추출

	        return claims.getSubject(); // subject에서 userId 추출

	    } catch (Exception e) {
	        LOGGER.error("토큰에서 userId를 추출하는 중 오류가 발생했습니다.", e);
	        throw new RuntimeException("토큰에서 userId를 추출하는 중 오류가 발생했습니다.", e);
	    }
	}

	
	
	

}
