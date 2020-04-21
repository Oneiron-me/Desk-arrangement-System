package com.oneiron.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport{

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SessionInterceptor sessionInterceptor;
	
	@Autowired
	HttpMessageConverter<?> htmlEscapingConverter;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		logger.info("Added Interceptor.");
		registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/").resourceChain(false);
		//registry.addResourceHandler("/static/**").addResourceLocations("/static/").resourceChain(false);
		
		
		/*
		 * registry .addResourceHandler("/**")
		 * .addResourceLocations("classpath:/static/","/webjars/") .setCachePeriod(3600)
		 * .resourceChain(true) .addResolver(new PathResourceResolver());
		 */
		
		registry.addResourceHandler("/.well-known/**").addResourceLocations("classpath:/.well-known/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	//message converter 등록 // xss 처리할거 올림
	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(htmlEscapingConverter);
	}

	//웰컴 설정!!!
	@Override
	protected void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("redirect:/intro/home");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}
	
	
	
	/*
	 * private HttpMessageConverter<?> htmlEscapingConverter(){ ObjectMapper mapper
	 * = new ObjectMapper(); // objectMapper에 특수문자 처리 적용
	 * mapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
	 * 
	 * // converter에 mapper 적용시킴 MappingJackson2HttpMessageConverter converter = new
	 * MappingJackson2HttpMessageConverter(); converter.setObjectMapper(mapper);
	 * return converter;
	 * 
	 * }
	 */

}
