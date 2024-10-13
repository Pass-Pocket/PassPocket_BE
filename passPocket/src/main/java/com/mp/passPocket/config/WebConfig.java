package com.mp.passPocket.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public Gson gson() {
        return new GsonBuilder().create(); // 필요에 따라 Gson 설정 추가 가능
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    	
//    	// Jackson의 기본 HttpMessageConverter 제거
//        converters.removeIf(converter -> converter instanceof org.springframework.http.converter.json.MappingJackson2HttpMessageConverter);
//        
    	// Gson HttpMessageConverter 작성
        converters.add(new GsonHttpMessageConverter(gson()));
    }
}
