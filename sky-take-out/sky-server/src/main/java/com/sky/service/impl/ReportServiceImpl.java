package com.sky.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        List<LocalDate> dateList = new ArrayList<>();//存储从开始到结束日期的数据
        //一直加到结束日期
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 核心：先把 LocalDate 转成 String，再拼接
        String dateStr = dateList.stream()
                .map(LocalDate::toString) // 每个 LocalDate 转成字符串
                .collect(Collectors.joining(",")); // 用逗号拼接
        turnoverReportVO.setDateList(dateStr);
        List<Double> amountList = new ArrayList<>();
        // 查询各个日期对应的营业额数据
        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map =new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double sum =orderMapper.sumByMap(map);
            sum = sum == null ? 0.0 : sum;
            amountList.add(sum);
        }
        // 把营业额数据集合转换成 String
        turnoverReportVO.setTurnoverList(
                amountList
                .stream()
                .map(amount -> String.valueOf(amount))
                .collect(Collectors.joining(",")));
        return turnoverReportVO;
    }

    @Override
    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        UserReportVO userReportVO = new UserReportVO();
        List<LocalDate> dateList = new ArrayList<>();//存储从开始到结束日期的数据
        //一直加到结束日期
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 核心：先把 LocalDate 转成 String，再拼接
        String dateStr = dateList.stream()
                .map(LocalDate::toString) // 每个 LocalDate 转成字符串
                .collect(Collectors.joining(",")); // 用逗号拼接
        userReportVO.setDateList(dateStr);
        // 查询各个日期对应的总用户数据
        List<Integer> totalUserList = new ArrayList<>();
        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map =new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            totalUserList.add(userMapper.countAllUserByDate(map));
        }
        // 把用户数据集合转换成 String
        userReportVO.setTotalUserList(
                totalUserList
                .stream()
                .map(amount -> String.valueOf(amount))
                .collect(Collectors.joining(",")));
        // 查询各个日期对应新增用户数据
        List<Integer> newUserList = new ArrayList<>();
        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map =new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            newUserList.add(userMapper.countNewUserByDate(map));
        }
        // 把用户数据集合转换成 String
        userReportVO.setNewUserList(
                newUserList
                .stream()
                .map(amount -> String.valueOf(amount))
                .collect(Collectors.joining(",")));
        return userReportVO;
    }

    @Override
    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        OrderReportVO orderReportVO = new OrderReportVO();
        //日期数据获取
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String dateStr = dateList.stream()
                .map(LocalDate::toString)
                .collect(Collectors.joining(","));
        orderReportVO.setDateList(dateStr);
        //每日订单数获取
        List<Integer> orderCountList = new ArrayList<>();
        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map =new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            orderCountList.add(orderMapper.countByDay(map));
        }
        orderReportVO.setOrderCountList(
                orderCountList
                .stream()
                .map(amount -> String.valueOf(amount))
                .collect(Collectors.joining(",")));
        //每日有效订单数获取
        List<Integer> validOrderCountList = new ArrayList<>();
        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map =new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            validOrderCountList.add(orderMapper.countByDayAndStatus(map));
        }
        orderReportVO.setValidOrderCountList(
                validOrderCountList
                .stream()
                .map(amount -> String.valueOf(amount))
                .collect(Collectors.joining(",")));
        //订单总数获取
        orderReportVO.setTotalOrderCount(orderCountList.stream().reduce(Integer::sum).get());
        //有效订单数获取
        orderReportVO.setValidOrderCount(validOrderCountList.stream().reduce(Integer::sum).get());
        //订单完成率获取
        orderReportVO.setOrderCompletionRate(orderReportVO.getValidOrderCount().doubleValue()/orderReportVO.getTotalOrderCount());
        return orderReportVO;
    }
}
