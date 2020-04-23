package com.oneiron.config;

import javax.servlet.http.HttpSessionListener;

import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
	
	//http2 적용
	@Bean
	public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
	    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    factory.addConnectorCustomizers(connector -> connector.addUpgradeProtocol(new Http2Protocol()));
	    return factory;
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
