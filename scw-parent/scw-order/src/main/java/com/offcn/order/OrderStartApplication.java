package com.offcn.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.offcn.order.mapper")
public class OrderStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderStartApplication.class);
    }
}
