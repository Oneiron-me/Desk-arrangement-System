package com.oneiron.login.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static String authorizationRequestBaseUri = "oauth2/authorization";
	Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
	
	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
	
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		
		Iterable<ClientRegistration> clientRegistrations = null;
		ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
		if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
			clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
		}

		clientRegistrations.forEach(registration -> 
			oauth2AuthenticationUrls.put(
				registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()
			)
		);
		
		model.addAttribute("urls", oauth2AuthenticationUrls);
		
		return "views/login/login";
	}
	
	@RequestMapping("/callback")
	public String callback(HttpServletRequest request, Model model) {
		
		logger.info("콜백");
		
		return "test";
	}
	
//	@RequestMapping("/login/oauth2/code/google")
//	public String oauthGoogleCallback(HttpServletRequest request, Model model, OAuth2AuthenticationToken authentication) {
//		OAuth2AuthorizedClient client = authorizedClientService
//			      .loadAuthorizedClient(
//			        authentication.getAuthorizedClientRegistrationId(), 
//			          authentication.getName());
//		
//		String userInfoEndpointUri = client.getClientRegistration()
//				  .getProviderDetails().getUserInfoEndpoint().getUri();
//		
//		logger.info("토큰 {}", userInfoEndpointUri);
//		logger.info("토큰 {}", authentication);
//		logger.info("토큰 {}", client);
//		
//		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
//		    RestTemplate restTemplate = new RestTemplate();
//		    HttpHeaders headers = new HttpHeaders();
//		    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
//		      .getTokenValue());
//		    HttpEntity entity = new HttpEntity("", headers);
//		    ResponseEntity<Map> response = restTemplate
//		      .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
//		    Map userAttributes = response.getBody();
//		    model.addAttribute("name", userAttributes.get("name"));
//		    
//		    logger.info("콜백1 {}",client.getPrincipalName());
//			logger.info("콜백2 {}", userAttributes.get("name"));
//		}
//		logger.info("콜백");
//		
//		return "test";
//	}
	
	
}

