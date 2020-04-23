package com.oneiron.config.security;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.oneiron.login.doc.UserDoc;
import com.oneiron.util.Util;

@Component
public class CustomAudenticationProvider implements AuthenticationProvider {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String loginId = authentication.getName();
		String password = (String) authentication.getCredentials();
		UserDetails user = null;
		UserDoc doc = null;
		Collection<? extends GrantedAuthority> authorities = null;
		String message = null;
		
		Util util = new Util();
		
		SCryptPasswordEncoder encoder = util.sCryptPasswordEncoder(65536, 64, 1, 32, 64);
		
		try {
			doc = (UserDoc)userDetailsService.loadUserByUsername(loginId);

			//1q2w3e4r
			//$104001$ikuih+xjEtmtbOIXvB7p+QrTgnmtCJiTN17VNdukdSHVEueuWEu7ZQPrHRds8TeH+fKavAOLYjZBXQWmEjsQ+A==$nVremxOAzJlNqM96AqgGFE+B8tom2ynah+UdRcmTh9Y=
			
			if(doc == null){
				message = "로그인 정보가 없습니다.";

				throw new AccountStatusException(message) {
					private static final long serialVersionUID = 47681837476778765L;
				};
			}
			
			if(doc.getJoinKinds() != null && !doc.getJoinKinds().equals("web")){
				message = "소셜 로그인을 이용해주세요.";

				throw new AccountStatusException(message) {
					private static final long serialVersionUID = -868660158678789528L;
				};
			}
			
			if(!encoder.matches(password, doc.getPassword())) {
				message = "비번이 틀렸읍니다.";

				throw new AccountStatusException(message) {
					private static final long serialVersionUID = -47681837476778765L;
				};
			}
			
			logger.info("user 정보 : {}", doc);
			
			user = doc;
			
			authorities = user.getAuthorities();
			
		} catch (UsernameNotFoundException e) {
			logger.info(e.toString());
			throw new UsernameNotFoundException(e.getMessage());
		} catch (BadCredentialsException e) {
			logger.info(e.toString());
			throw new BadCredentialsException(e.getMessage());
		} catch (AuthenticationServiceException e) {
			logger.info(e.toString());
			throw new AuthenticationServiceException(e.toString());
		}
		return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
}