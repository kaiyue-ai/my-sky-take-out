package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据分类查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> getSetmealByCategoryId(Setmeal setmeal);

    /**
     * 根据套餐id查询包含的菜品列表，返回DishItemVO
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemBySetmealId(Long id);
}
