package com.oneiron.login.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static String authorizationRequestBaseUri = "oauth2/authorization";
	Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
	
	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
	
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
	
}

