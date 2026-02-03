package com.sky.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    private WorkspaceService workspaceService;
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

    @Override
    /**
     * 销量top10
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        //获取销量top10
        List<GoodsSalesDTO> mapList = orderMapper.getSalesTop10(map);
        salesTop10ReportVO.setNameList(mapList.stream().map(GoodsSalesDTO::getName).collect(Collectors.joining(",")));
        salesTop10ReportVO.setNumberList(mapList.stream().map(GoodsSalesDTO::getNumber).map(String::valueOf).collect(Collectors.joining(",")));
        return salesTop10ReportVO;
    }

    @Override
    /**
     * 导出营业数据
     * @param response
     */
    public void exportBusinessData(HttpServletResponse response) {
        //1. 查询数据库,获取营业数据(最近30天)
        LocalDate dataBegin = LocalDate.now().minusDays(30);
        LocalDate dataEnd = LocalDate.now().minusDays(1);
        LocalDateTime localBegin = LocalDateTime.of(dataBegin, LocalTime.MIN);
        LocalDateTime localEnd = LocalDateTime.of(dataEnd, LocalTime.MAX);
        BusinessDataVO businessData = workspaceService.getBusinessData(localBegin, localEnd);
        //2. 通过POI将数据写入到Excel文件
        //获取输入流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        //基于模板文件创建Excel对象
        try {
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = excel.getSheet("Sheet1");
            //填充数据
            //时间
            sheet.getRow(1).getCell(1).setCellValue("时间:"+dataBegin + "至" + dataEnd);
            //获得第四行
            XSSFRow row4 = sheet.getRow(3);
            //营业额
            row4.getCell(2).setCellValue(businessData.getTurnover());
            //订单完成率
            row4.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            //新增用户数
            row4.getCell(6).setCellValue(businessData.getNewUsers());
            //获得第五行
            XSSFRow row5 = sheet.getRow(4);
            //有效订单数
            row5.getCell(2).setCellValue(businessData.getValidOrderCount());
            //平均客单价
            row5.getCell(4).setCellValue(businessData.getUnitPrice());
            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dataBegin.plusDays(i);
                LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
                //获取指定天的营业数据
                BusinessDataVO data = workspaceService.getBusinessData(begin, end);
                //获取某一行
                XSSFRow row = sheet.getRow(7 + i);
                //获取这行上的单元格
                //日期
                row.getCell(1).setCellValue(date.toString());
                //营业额
                row.getCell(2).setCellValue(data.getTurnover());
                //有效订单数
                row.getCell(3).setCellValue(data.getValidOrderCount());
                //订单完成率
                row.getCell(4).setCellValue(data.getOrderCompletionRate());
                //平均客单价
                row.getCell(5).setCellValue(data.getUnitPrice());
                //新增用户数
                row.getCell(6).setCellValue(data.getNewUsers());
            }
            //3. 通过输出流将Excel文件下载到客户端
            ServletOutputStream os = response.getOutputStream();
            excel.write(os);
            os.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
