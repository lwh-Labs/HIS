package com.sdut.hospital.service;

import com.sdut.hospital.mapper.AdministratorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sdut.hospital.po.Administrator;


@Service
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    AdministratorMapper administratorMapper;
    @Override
    public Administrator check(String name,String password){
        return administratorMapper.findByNameAndPassword(name, password);
    }

    @Override
    public void save(Administrator administrator) {
        administratorMapper.save(administrator);
    }
}
