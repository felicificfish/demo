package com.example.demo;

import com.example.demo.configs.mapper.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@MapperScan(basePackages = "com.example.demo.dao")
@SpringBootApplication(scanBasePackages = {"com.example.demo"})
public class DemoApplication {
    /**
     * 开启WebSocket支持
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
