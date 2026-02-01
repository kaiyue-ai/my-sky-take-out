package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

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

    @Override
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        //保存套餐并返回主键字段
        //Long id = setmealMapper.insert(setmeal); 这样写返回的是行数
        //保存套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {setmealDish.setSetmealId(setmeal.getId());});
        setmealDishMapper.insert(setmealDishes);
    }

    @Override
    /**
     * 根据id查询
     */
    public Setmeal getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        return setmeal;
    }

    @Override
    /**
     * 根据id查询菜品列表
     * @param id
     * @return
     */
    public List<SetmealDish> getDishItemBySetmealId(Long id) {
        List<SetmealDish> list = setmealDishMapper.getDishesBySetmealId(id);
        return list;
    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询：{}",setmealPageQueryDTO);
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> list = setmealMapper.pageQuery(setmealPageQueryDTO);
        List<SetmealVO> result = list.getResult();
        if (result != null && result.size() > 0) {
            for (SetmealVO setmealVO : result) {
                Long categoryId = setmealVO.getCategoryId();
                setmealVO.setCategoryName(categoryMapper.getNameById(categoryId));
            }
        }
        return new PageResult(list.getTotal(),list.getResult());
    }

    @Override
    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        List<Long> ids = new ArrayList<>();
        ids.add(setmealDTO.getId());
        //删除套餐数据和菜品关系数据
        setmealDishMapper.deleteBySetmealIds(ids);
        //修改套餐数据并返回主键数据
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.updateById(setmeal);
        //保存套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {setmealDish.setSetmealId(setmeal.getId());});
        setmealDishMapper.insert(setmealDishes);
    }

    @Override
    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    public void deleteById(List<Long> ids) {
        //删除套餐数据
        setmealMapper.deleteById(ids);
        //删除套餐和菜品关系数据
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.updateById(setmeal);
    }
}
