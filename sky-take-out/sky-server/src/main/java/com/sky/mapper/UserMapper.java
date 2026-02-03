package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from sky_take_out.user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入用户数据
     * @param user
     */
    void insert(User user);

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @Select("select * from sky_take_out.user where id = #{userId}")
    User getById(Long userId);

    /**
     * 根据日期统计用户数量
     * @param map
     * @return
     */
    @Select("select count(id) from sky_take_out.user where create_time <=  #{end}")
    Integer countAllUserByDate(Map map);

    /**
     * 根据日期统计新增用户数量
     * @param map
     * @return
     */
    @Select("select count(id) from sky_take_out.user where create_time between #{begin} and #{end}")
    Integer countNewUserByDate(Map map);

    /**
     * 根据条件统计用户数量
     * @param map
     * @return
     */
    @Select("select count(id) from sky_take_out.user where create_time <=  #{end} and create_time >= #{begin}")
    Integer countByMap(Map map);
}
