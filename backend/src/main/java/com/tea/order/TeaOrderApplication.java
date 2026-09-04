package com.tea.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("com.tea.order.mapper")
@SpringBootApplication
public class TeaOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeaOrderApplication.class, args);
    }
}
