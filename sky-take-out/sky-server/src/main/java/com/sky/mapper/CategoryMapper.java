package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 新增分类
     * @param category
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into sky_take_out.category ( type, name, sort, status, create_time, update_time, create_user, update_user) VALUES (#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * 根据id删除分类
     * @param id
     */
    @Delete("delete from sky_take_out.category where id=#{id}")
    void deleteById(Long id);

    /**
     * 修改分类
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);
    /**
     * 查询分类
     * @param type
     * @return
     */
    @Select("select * from sky_take_out.category where type=#{type} order by sort")
    List<Category> list(Integer type);


    /**
     * 分类分页查询
     * @param build
     * @return
     */
    Page<Category> pageQuery(Category build);
}
