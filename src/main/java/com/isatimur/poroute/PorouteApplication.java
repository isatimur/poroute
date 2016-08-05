package com.isatimur.poroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class PorouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PorouteApplication.class, args);
    }

}
