package com.oneiron.config.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.oneiron.login.doc.UserDoc;
import com.oneiron.service.UserRepositoryService;

@Service
public class CustomOidcUserService extends OidcUserService {
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="userRepositoryServiceImpl")
	UserRepositoryService userRepositoryServiceImpl;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(userRequest);
		Map<String, Object> attributes = oidcUser.getAttributes();
		UserDoc userInfo = null;

		String name = attributes.get("family_name").toString() + attributes.get("given_name").toString();
		String picture = attributes.get("picture").toString();
		String locale = attributes.get("locale").toString();
		
		userInfo = userRepositoryServiceImpl.findUserByIdN1ql(attributes.get("email").toString());

		if(userInfo == null) {
			UserDoc doc = new UserDoc();
			doc.setId(attributes.get("email").toString());
			doc.setName(name);
			doc.setRole("MEMBER");
			doc.setJoinKinds("google");
			doc.setPicture(picture);
			doc.setLocale(locale);
			doc.setDocType("user");
			doc.setDeskList(new ArrayList<String>());
			logger.info("new google User account {}", doc);
			userRepositoryServiceImpl.save(doc);
		}
		
		logger.info("user 정보 : {}", userInfo);
		
		return oidcUser;
	}

}
