package com.oneiron.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionListener implements HttpSessionListener {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	@Autowired
//	SessionInfo sessionInfo;
//	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setMaxInactiveInterval(60^60);
//		UserDoc doc =  (UserDoc)se.getSession().getAttribute("userInfo");
//		logger.info("세션이 만들어졌당께 {}", doc);
		HttpSessionListener.super.sessionCreated(se);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
//		UserDoc doc =  (UserDoc)se.getSession().getAttribute("userInfo");
//		logger.info("세션 박살 {}", doc);
//		sessionInfo.remove(doc.getId());
		HttpSessionListener.super.sessionDestroyed(se);
	}

}
