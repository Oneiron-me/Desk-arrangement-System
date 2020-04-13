package com.oneiron.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.oneiron.util.SuperHashMap;
import com.oneiron.ws.SessionInfo;

@Component
public class StompInterceptor implements ChannelInterceptor{

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SessionInfo sessionInfo;
	
//	@Autowired
//	CommonFunction commonFunction;
	
	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String sessionId = accessor.getSessionId();
		
		accessor.getMessageHeaders().get("nativeHeaders");
		
		MessageHeaders header = accessor.getMessageHeaders();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = header.get("nativeHeaders", Collections.unmodifiableMap(new HashMap<String, Object>()).getClass());
		
//		String userId = commonFunction.userId();
//		String userName = commonFunction.userName();
		
		switch(accessor.getCommand()) {
			case CONNECT :
				logger.info("접속했음 {}" , userMap);
				logger.info("접속했음 {}" , userMap.get("uName"));
				logger.info("접속했음 {}" , userMap.get("uId"));
				sessionInfo.put(userMap.get("uId").toString().replaceAll("\\[", "").replaceAll("\\]", ""), userMap.get("uName").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
			break;
			
			case DISCONNECT :
//				logger.info("나갔음 {}" , userMap);
//				logger.info("나갔음 {}" , userMap.get("uName"));
//				logger.info("나갔음 {}" , userMap.get("uId"));
//				sessionInfo.remove(userId);
			break;
			
			default :
			break;
		}
	}
	
}
