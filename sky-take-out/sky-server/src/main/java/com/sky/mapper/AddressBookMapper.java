package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    void add(AddressBook addressBook);

    @Select("select * from sky_take_out.address_book where user_id = #{userId}")
    List<AddressBook> list(Long userId);


    @Select("select * from sky_take_out.address_book where is_default = 1 and user_id = #{userId}")
    AddressBook getDefault(Long userId);

    void update(AddressBook addressBook);

    @Delete("delete from sky_take_out.address_book where id = #{id}")
    void delete(Long id);

    @Select("select * from sky_take_out.address_book where id = #{id}")
    AddressBook getById(Long id);

    @Select("update sky_take_out.address_book set is_default = 0 where user_id = #{currentId} and is_default=1")
    void updateNotDefaultByUserId(Long currentId);

    @Update("update sky_take_out.address_book set is_default = 1 where id = #{id}")
    void updateIsDefaultByUserId(Long id);
}
