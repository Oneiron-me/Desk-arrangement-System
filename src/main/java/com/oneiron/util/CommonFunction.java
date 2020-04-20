package com.oneiron.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import com.oneiron.login.doc.UserDoc;

@Service
public class CommonFunction {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String userId() {
		String userId = null;
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(user instanceof DefaultOidcUser) {
			DefaultOidcUser oidcUser = (DefaultOidcUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = oidcUser.getEmail();
		} else if(user instanceof UserDoc) {
			UserDoc doc = (UserDoc)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = doc.getId();
		}
		
		return userId;
	}
	
	public String userName() {
		String userName = null;
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(user instanceof DefaultOidcUser) {
			DefaultOidcUser oidcUser = (DefaultOidcUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userName = oidcUser.getFullName();
		} else if(user instanceof UserDoc) {
			UserDoc doc = (UserDoc)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userName = doc.getUsername();
		}
		
		return userName;
	}
	
	public String today() {
		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(today);
	}
}
