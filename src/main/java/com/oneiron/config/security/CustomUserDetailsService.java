package com.oneiron.config.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oneiron.login.doc.UserDoc;
import com.oneiron.repo.Repository;
import com.oneiron.service.UserRepositoryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier(BeanNames.COUCHBASE_TEMPLATE)
	private CouchbaseTemplate defaultTemplate;

	@Autowired
	Repository repository;
	
	
	@Resource(name="userRepositoryServiceImpl")
	UserRepositoryService userRepositoryServiceImpl;
	
	/*
	 * @Autowired SCryptPasswordEncoder passwordEncoder;
	 */
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

		UserDoc doc = userRepositoryServiceImpl.findUserByIdN1ql(loginId);
		
		if(doc != null) { 
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(doc.getRole()));
			doc.setAuthorities(authorities); 
		}
		
		return doc;
	}

}
