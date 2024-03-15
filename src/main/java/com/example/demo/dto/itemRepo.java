package com.example.demo.dto;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.item;

public interface itemRepo extends CrudRepository<item, Long>{
    @Override
    public List<item> findAll();

    @Query(nativeQuery=true, value="SELECT maintenance_urgency as label, COUNT(*) as val from item group by maintenance_urgency")
    public List<urgencyCount> findUrgencyCount();
}
