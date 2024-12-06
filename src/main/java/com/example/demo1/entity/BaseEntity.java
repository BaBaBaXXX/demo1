package com.example.demo1.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;


public interface BaseEntity <T extends Serializable> {

    void setId(T id);

    T getId();


}
