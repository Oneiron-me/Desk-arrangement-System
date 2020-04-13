package com.oneiron.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private String id;
	private String password;
	private String redirect;
	private String exceptionMsg;
	private String defaultFailureUrl;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String user_id = request.getParameter(id);
		String pwd = request.getParameter(password);
		String loginRedirect = request.getParameter(redirect);
		
		request.setAttribute(id, user_id);
		request.setAttribute(password, pwd);
		request.setAttribute(redirect, loginRedirect);
		
		// Request 객체의 Attribute에 예외 메시지 저장
		request.setAttribute(exceptionMsg, exception.getMessage());
		
		request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
		
	}

}
