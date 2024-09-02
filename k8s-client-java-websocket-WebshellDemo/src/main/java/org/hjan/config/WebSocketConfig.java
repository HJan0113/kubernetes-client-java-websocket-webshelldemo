package org.hjan.config;

/**
 * @Author HJan
 * @Date 2024/9/1 12:35
 * @Description
 */
import org.hjan.controller.CommandWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CommandWebSocketHandler(), "/connect/pods").setAllowedOrigins("*");
    }
}
