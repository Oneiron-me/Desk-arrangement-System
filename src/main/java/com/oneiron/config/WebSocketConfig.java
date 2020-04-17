package com.oneiron.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
    private StompInterceptor stompInterceptor;
	
//	@Autowired
//	WebSocketHandlerCustomDecorator webSocketHandlerCustomDecorator;
	
//	@Autowired
//	private HttpHandshakeInterceptor httpHandshakeInterceptor;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		
		registry.addEndpoint("/oneIronWS/**").setAllowedOrigins("*")
//		.setHandshakeHandler(httpHandshakeInterceptor)
		.withSockJS(); // 클라이언트로부터 받을 prefix
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		registry.enableSimpleBroker("/topic");	// 해당 api를 구독하는 클라이언트들에게 메시지 전송
		registry.setApplicationDestinationPrefixes("/send");	//클라이언트로부터 받을 prefix
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompInterceptor); //인터셉터 갖다붙임
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		//registry.addDecoratorFactory(webSocketHandlerCustomDecorator);
		WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
	}
}
