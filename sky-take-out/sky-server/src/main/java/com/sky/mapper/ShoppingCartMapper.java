package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 根据用户id和菜品或套餐id查询购物车数据
     * @param shoppingCart
     * @return
     */
     List<ShoppingCart> getByUserIdAndDishIdOrSetmealId(ShoppingCart shoppingCart);

     /**
     * 更新购物车数据
     * @param cart
     */
     @Update("update sky_take_out.shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart cart);

     /**
     * 插入购物车数据
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车数据
     * @param currentId
     */

    @Delete("delete from sky_take_out.shopping_cart where user_id = #{currentId}")
    void deleteByUserId(Long currentId);

    /**
     * 根据id删除购物车数据
     * @param id
     */
    @Delete("delete from sky_take_out.shopping_cart where id in (${id})")
    void deleteByIds(Long id);
}
