package com.oneiron.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

//@Component
public class WebSocketHandlerCustomDecorator implements WebSocketHandlerDecoratorFactory{

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public WebSocketHandler decorate(WebSocketHandler handler) {
		
		return new WebSocketHandlerDecorator(handler) {

			@SuppressWarnings("static-access")
			@Override
			public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
				logger.info("session close");
				session.close(closeStatus.NOT_ACCEPTABLE);
				super.afterConnectionClosed(session, closeStatus);
			}
			
		};
	}

}
