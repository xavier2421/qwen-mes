package com.mes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 销售与订单管理服务启动类
 */
@SpringBootApplication
@EntityScan(basePackages = "com.mes.entity")
@EnableJpaRepositories(basePackages = "com.mes.repository")
public class SalesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesServiceApplication.class, args);
    }
}
