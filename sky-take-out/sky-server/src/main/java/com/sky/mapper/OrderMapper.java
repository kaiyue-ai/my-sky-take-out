package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper

public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 获取订单列表
     * @param orders
     * @return
     */
    Page<Orders> list(Orders orders);

    /**
     * 分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     * @param toBeConfirmed
     * @return
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer toBeConfirmed);

    /**
     * 查询一定时间的未付款订单
     * @param status
     * @param time
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime time);

    /**
     * 批量更新订单信息
     * @param ordersList
     */
    void updateBatch(List<Orders> ordersList);

    /**
     * 根据日期统计营业额
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 根据日期统计订单数量
     * @param map
     * @return
     */
    @Select("select count(id) from orders where order_time between #{begin} and #{end}")
    Integer countByDay(Map map);

    /**
     * 根据日期统计订单数量
     * @param map
     * @return
     */
    @Select("select count(id) from orders where order_time between #{begin} and #{end} and status = #{status}")
    Integer countByDayAndStatus(Map map);
}
