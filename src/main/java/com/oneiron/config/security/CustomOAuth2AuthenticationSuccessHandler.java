package com.oneiron.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.oneiron.login.doc.UserDoc;
import com.oneiron.service.UserRepositoryService;

@Component
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Resource(name="userRepositoryServiceImpl")
	UserRepositoryService userRepositoryServiceImpl;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if (response.isCommitted()) {
			return;
		}
		DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
		Map<String, Object> attributes = oidcUser.getAttributes();
		String email = attributes.get("email").toString();
		
		logger.info("google login success handler {}", oidcUser);
		
		UserDoc doc = userRepositoryServiceImpl.findGoogleUserByIdN1ql(email);
		
		if(doc != null) { 
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(doc.getRole()));
			doc.setAuthorities(authorities); 
		}
		
		//세션이상하면 걍 박살냄
		sessionInvalidate(request);
		
		HttpSession session = request.getSession(true);
		
		session.setAttribute("userInfo", doc);
		
		getRedirectStrategy().sendRedirect(request, response, "/intro/home");
	}
	
	private void sessionInvalidate(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		if(session == null)
			return;
		
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}
