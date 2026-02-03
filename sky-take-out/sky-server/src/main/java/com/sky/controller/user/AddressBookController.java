package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "C端-地址簿接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;
    @PostMapping
    @ApiOperation("新增地址")
    @CacheEvict(cacheNames = "addressBookCache",allEntries = true)
    public Result add(@RequestBody AddressBook addressBook){
        log.info("新增地址");
        addressBookService.add(addressBook);
        return Result.success();
    }
    @ApiOperation("查看地址")
    @GetMapping("/list")
    @Cacheable(cacheNames = "addressBookCache",key = "'list_'+T(com.sky.context.BaseContext).currentId")
    public Result<List<AddressBook>> list(){
        log.info("查看地址");
        return Result.success(addressBookService.list());
    }
    @GetMapping("/default")
    @ApiOperation("查看默认地址")
    @Cacheable(cacheNames = "addressBookCache",key = "'default_'+T(com.sky.context.BaseContext).currentId")
    public Result<AddressBook> getDefault(){
        log.info("查看默认地址");
        return Result.success(addressBookService.getDefault());
    }
    @PutMapping
    @ApiOperation("根据id修改地址")
    @CacheEvict(cacheNames = "addressBookCache",allEntries = true)
    public Result update(@RequestBody AddressBook addressBook){
        log.info("根据id修改地址");
        addressBookService.update(addressBook);
        return Result.success();
    }
    @DeleteMapping
    @ApiOperation("删除地址")
    @CacheEvict(cacheNames = "addressBookCache",allEntries = true)
    public Result delete(Long id){
        log.info("删除地址");
        addressBookService.delete(id);
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    @Cacheable(cacheNames = "addressBookCache",key = "#id")
    public Result<AddressBook> getById(@PathVariable Long id){
        log.info("根据id查询地址");
        return Result.success(addressBookService.getById(id));
    }
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    @CacheEvict(cacheNames = "addressBookCache",allEntries = true)
    public Result setDefault(@RequestBody AddressBook addressBook){//这里使用请求体请求时候必须用键值对或者对象来接受，不能直接用数据类型
        log.info("设置默认地址");
        Long id = addressBook.getId();
        addressBookService.setDefault(id);
        return Result.success();
    }
}
