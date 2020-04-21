package com.oneiron.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CustomUserDetailsService userDetailService;
	
	@Autowired
	CustomAudenticationProvider audenticationProvider;
	
	@Autowired
	CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Autowired
	CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Autowired
	CustomOidcUserService customOidcUserService;
	
	@Autowired
	CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
	
	public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		customAuthenticationSuccessHandler.setDefaultUrl("/");
		customAuthenticationSuccessHandler.setTarget("/intro/home");
		return customAuthenticationSuccessHandler;
	}
	
	public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
		customAuthenticationFailureHandler.setId("loginId");
		customAuthenticationFailureHandler.setPassword("password");
		customAuthenticationFailureHandler.setRedirect("fail");
		customAuthenticationFailureHandler.setExceptionMsg("securityExceptionMsg");
		customAuthenticationFailureHandler.setDefaultFailureUrl("/login");
		return customAuthenticationFailureHandler;
	}
	
	public CustomAudenticationProvider audenticationProvider() {
		return audenticationProvider;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		//나중에 필요할때 쓰자. 이건 항상통과임.
		//web.ignoring().antMatchers("/webjars/**", "/static/**", "/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		 * http.authorizeRequests() .antMatchers("/user/**").access("ROLE_USER")
		 * .antMatchers("/admin/**").access("ROLE_ADMIN") .antMatchers("/", "/login",
		 * "/error").permitAll() .antMatchers("/**").authenticated();
		 */
		http.authorizeRequests()
			.antMatchers("/admin/home").access("hasAuthority('ADMIN')");
		
		http.authorizeRequests()
			.antMatchers("/", "/test","/login", "/.well-known/**", "/callback", "/error", "/webjars/**", "/static/**").permitAll()
			.anyRequest().authenticated();
        
		// 웹소켓은 csrf 뗐다!!!
		http.csrf()
				.ignoringAntMatchers("/oneIronWS/**")
			.and()
				.headers()
				.frameOptions()
				.sameOrigin()
			.and()
			.authorizeRequests()
			.and()
				.oauth2Login()
				.loginPage("/login")
				.successHandler(customOAuth2AuthenticationSuccessHandler)
				.permitAll()
            	.userInfoEndpoint()
            	.oidcUserService(customOidcUserService)
            	.and()
            	.authorizationEndpoint()
            	.authorizationRequestRepository(authorizationRequestRepository())
            .and()
			.and() //로그인 관련
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.successHandler(customAuthenticationSuccessHandler()) //성공 핸들러
				.failureHandler(customAuthenticationFailureHandler()) // 실패 핸들러
				.permitAll()
			.and()
				.logout() // logout설정
				.clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
			.and() // 403 forbidden
				.exceptionHandling()
				.accessDeniedPage("/denied")
			.and()
				.sessionManagement()
				.maximumSessions(1)
				.maxSessionsPreventsLogin(true)
				.sessionRegistry(sessionRegistry());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 일반로그인만 authentication provider 놧따!
		auth.userDetailsService(userDetailService)
			.and()
				.authenticationProvider(audenticationProvider());
	}
	
	@Bean
	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
	
	@Bean
	public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
		return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	}
	
	
}
