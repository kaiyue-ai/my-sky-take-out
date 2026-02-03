package com.sky.task;
import com.sky.entity.Orders;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderService orderService;
    /**
     * 定时处理超时订单
     */
    @Scheduled(cron = "0 * * * * ? ")//每分钟执行一次
    public  void processTimeoutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);
        orderService.processTimeoutOrder(Orders.PENDING_PAYMENT,localDateTime);
    }
    /**
     * 定时处理支付超时派送
     */
    @Scheduled(cron = "0 0 1 * * ? ")//每天凌晨1点执行一次
    public void processTimeoutDeliveryOrder(){
        log.info("定时处理支付超时派送：{}", LocalDateTime.now());
        //获取所有派送中的订单
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-60);
        orderService.processDeliveryTimeoutOrder(Orders.DELIVERY_IN_PROGRESS,localDateTime);
    }
}
