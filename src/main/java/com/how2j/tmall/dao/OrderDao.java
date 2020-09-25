package com.how2j.tmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.pojo.User;

public interface OrderDao extends JpaRepository<Order,Integer>{
	public List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}
