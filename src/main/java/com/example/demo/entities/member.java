package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long member_id;
    public String name;

    public Long getWatts_used() {
        return watts_used;
    }

    public void setWatts_used(Long watts_used) {
        this.watts_used = watts_used;
    }

    Long watts_used;

    int noOfPeople;

    Long electricityConsumerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(int noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public Long getElectricityConsumerId() {
        return electricityConsumerId;
    }

    public void setElectricityConsumerId(Long electricityConsumerId) {
        this.electricityConsumerId = electricityConsumerId;
    }
}
