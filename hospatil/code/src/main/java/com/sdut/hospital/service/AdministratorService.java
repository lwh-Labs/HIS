package com.sdut.hospital.service;

import com.sdut.hospital.po.Administrator;



public interface AdministratorService {
    Administrator check(String name,String password);

    void save(Administrator administrator);
}
