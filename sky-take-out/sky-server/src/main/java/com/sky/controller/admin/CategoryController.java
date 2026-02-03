package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 添加分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("添加分类")
    @CacheEvict(cacheNames = "category",allEntries = true)
    public Result add(@RequestBody CategoryDTO categoryDTO){
        log.info("添加分类{}",categoryDTO);
        categoryService.add(categoryDTO);
        return Result.success();
    }
    /**
     * 删除分类
     * @param id
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    @CacheEvict(cacheNames = "category",allEntries = true)
    public Result delete(Long id){
        log.info("删除分类{}",id);
        categoryService.delete(id);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    @PutMapping
    @ApiOperation("修改分类")
    @CacheEvict(cacheNames = "category",allEntries = true)
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类{}",categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }
    /**
     * 分类查询
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("分类查询")
    @Cacheable(cacheNames = "category",key = "'type_'+#type")
    public Result<List<Category>> list(Integer type){
        log.info("分类查询{}",type);
        List<Category> list=categoryService.list(type);
        return Result.success(list);
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    @CacheEvict(cacheNames = "category",allEntries = true)
    public Result startOrStop(@PathVariable("status") Integer status,Long id){
        log.info("分类状态：{}，分类id：{}",status,id);
        categoryService.startOrStop(status,id);
        return Result.success();
    }
    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    @Cacheable(cacheNames = "category",key = "'page_'+#categoryPageQueryDTO")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }
}
