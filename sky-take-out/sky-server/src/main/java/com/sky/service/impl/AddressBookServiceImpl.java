package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(StatusConstant.DISABLE);
        addressBookMapper.add(addressBook);
    }

    @Override
    public List<AddressBook> list() {
        Long userId = BaseContext.getCurrentId();
        List<AddressBook> list = addressBookMapper.list(userId);
        return list;
    }

    @Override
    public AddressBook getDefault() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.getDefault(userId);
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    @Override
    public void delete(Long id) {
        addressBookMapper.delete(id);
    }

    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    @Override
    @Transactional
    public void setDefault(Long id) {
        //先将当前用户的默认地址设置为非默认
        addressBookMapper.updateNotDefaultByUserId(BaseContext.getCurrentId());
        //再修改当前地址为默认地址
        addressBookMapper.updateIsDefaultByUserId(id);
    }
}
