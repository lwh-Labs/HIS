package com.sdut.hospital.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sdut.hospital.po.Administrator;


@Repository
public interface AdministratorMapper extends JpaRepository<Administrator, Long> {
    Administrator findByNameAndPassword(String name, String password);
}
