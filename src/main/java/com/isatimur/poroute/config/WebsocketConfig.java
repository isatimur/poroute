package com.isatimur.poroute.config;

import com.isatimur.poroute.websocket.commands.LocationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by developer on 8/4/16.
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(myHandler(), "/myHandler");
        registry.addHandler(locationHandler(), "/location");
    }

//    @Bean
//    public WebSocketHandler myHandler() {
//        return new MyHandler();
//    }

    @Bean
    public WebSocketHandler locationHandler() {
        return new LocationHandler();
    }

}
