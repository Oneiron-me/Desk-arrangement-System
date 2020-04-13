package com.oneiron.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneiron.util.HTMLCharacterEscapes;
import com.oneiron.ws.SessionInfo;

@Component
public class CollectBeanComponent {

	@Bean
	public SessionInfo sessionInfo() {
		return SessionInfo.getInstance();
	}
	
	@Bean
	public PasswordEncoder passWordEncoder() {;
		//default setting
		return new SCryptPasswordEncoder(2, 8, 1, 32, 64);
	}
	
	@Bean
	GrantedAuthorityDefaults grantedAuthorityDefaults() {
	    return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
	}
	
	@Bean
	public HttpMessageConverter<?> htmlEscapingConverter(){
		ObjectMapper mapper = new ObjectMapper();
		// objectMapper에 특수문자 처리 적용
		mapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		
		// converter에 mapper 적용시킴
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(mapper);
		return converter;
	}
	
	
	@Bean
	public HttpSessionListener httpSessionListener() {
		return new SessionListener();
	}
	
}
