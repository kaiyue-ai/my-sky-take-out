package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    public void add(ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车：{}", shoppingCartDTO);
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //判断当前购物车数据是否已存在
        List<ShoppingCart> shoppingCarts= shoppingCartMapper.getByUserIdAndDishIdOrSetmealId(shoppingCart);
        //如果已经存在，将数量+1
        if(shoppingCarts != null && shoppingCarts.size() > 0){
            ShoppingCart cart = shoppingCarts.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.update(cart);
        }else {
            //如果不存在，需要将购物车数据插入数据库
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            Long dishId = shoppingCartDTO.getDishId();
            if(dishId != null){
                //如果是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCartMapper.insert(shoppingCart);
            }else{
                //如果是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCartMapper.insert(shoppingCart);
            }
        }
    }
}
