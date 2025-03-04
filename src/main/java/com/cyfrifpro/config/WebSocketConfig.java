package com.cyfrifpro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// Enable a simple in-memory message broker and configure destination prefixes
		config.enableSimpleBroker("/topic", "/queue");
		// Application destination prefix to route messages to @MessageMapping methods
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// Register an endpoint that clients will use to connect to the WebSocket
		// server.
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
	}
}
