package com.mp.passPocket.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
	
	
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider =jwtTokenProvider;
	}
	
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http
         .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (필요에 따라 활성화 가능)
         .authorizeHttpRequests(auth -> auth
             .requestMatchers("/v3/api-docs/**", 
                     "/swagger-ui/**", 
                     "/swagger-ui.html"
                     ,"auth/**").permitAll() // 해당 경로들은 인증 없이 접근 가능
             .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
         );

     return http.build();
		
	}
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(
                "/v3/api-docs/**", 
                "/swagger-resources/**", 
                "/swagger-ui.html", 
                "/swagger-ui/**", 
                "/webjars/**"
            ); // Swagger 예외 처리
    }
		

}
