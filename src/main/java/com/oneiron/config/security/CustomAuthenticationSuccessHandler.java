package com.oneiron.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.oneiron.login.doc.UserDoc;

import lombok.Data;

@Component
@Data
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String target;
	private String defaultUrl;
	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	public CustomAuthenticationSuccessHandler() {
		this.target = "";
		this.defaultUrl = "/";
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//세션이상하면 걍 박살냄
		sessionInvalidate(request);
		
		HttpSession session = request.getSession(true);
		UserDoc doc = (UserDoc)authentication.getPrincipal();
		
		session.setAttribute("userInfo", doc);
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if(savedRequest != null) {
			requestCache.removeRequest(request, response);
		}
		
		redirectStrategy.sendRedirect(request, response, getTarget());
		
	}
	
	void sessionInvalidate(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		if(session == null)
			return;
		
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}
