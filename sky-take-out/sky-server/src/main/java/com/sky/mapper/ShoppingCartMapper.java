package com.sky.mapper;

import com.sky.entity.ShoppingCart;
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
}
