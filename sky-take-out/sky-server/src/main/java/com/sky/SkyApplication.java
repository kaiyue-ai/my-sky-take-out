package com.sky;

import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@EnableCaching //开启缓存
@Slf4j
@EnableScheduling//开启任务调度
public class SkyApplication {
    public static void main(String[] args) {
        log.info("项目启动时间：{}", LocalDateTime.now());
        SpringApplication.run(SkyApplication.class, args);
    }
}
