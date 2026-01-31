package com.sky.service.impl;

import com.sky.entity.Setmeal;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list =setmealMapper.getSetmealByCategoryId(setmeal);
        return list;
    }

    @Override
    /**
     * 根据套餐id查询包含的菜品列表
     * @param id
     * @return
     */
    public List<DishItemVO> getItemById(Long id) {
        List<DishItemVO> list=setmealMapper.getDishItemBySetmealId(id);
        return list;
    }
}
