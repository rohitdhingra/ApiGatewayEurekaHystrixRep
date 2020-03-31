package com.example.os.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.os.api.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Integer>{

}
