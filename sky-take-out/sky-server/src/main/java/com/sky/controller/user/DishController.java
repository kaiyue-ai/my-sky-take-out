package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端菜品接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId){
        log.info("根据分类id查询菜品：{}",categoryId);
        //构造redis的key
        String key="dish_"+categoryId;
        //查询redis是否存在菜品数据
        List<DishVO>  redisList = (List<DishVO>)redisTemplate.opsForValue().get(key);
        //如果缓存中存在，则直接返回
        if(redisList != null && redisList.size()>0){
            log.info("缓存命中");
            return Result.success(redisList);
        } else {
            Dish dish=new Dish();
            dish.setCategoryId(categoryId);
            dish.setStatus(StatusConstant.ENABLE);
            List<DishVO> list=dishService.listWithFlavors(dish);
            //将数据放到redis当中
            redisTemplate.opsForValue().set(key,list);
            return Result.success(list);
        }
        //缓存中没有，则查询数据库
    }
}
