package com.mes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 生产管理服务启动类
 * 功能：生产计划、工单管理、工序管理、生产执行跟踪
 */
@SpringBootApplication
@EntityScan(basePackages = "com.mes.entity")
@EnableJpaRepositories(basePackages = "com.mes.repository")
public class ProductionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductionServiceApplication.class, args);
    }
}
