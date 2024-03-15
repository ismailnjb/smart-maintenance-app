package com.example.demo.dto;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.member;

public interface memberRepo extends CrudRepository<member , Long> {
    @Override
    List<member> findAll();

    @Query(nativeQuery = true,value="select * from member order by WATTS_USED ASC")
    List<member> findAscending();
}
