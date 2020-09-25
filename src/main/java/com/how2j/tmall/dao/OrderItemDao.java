package com.how2j.tmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.pojo.OrderItem;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.User;

public interface OrderItemDao extends JpaRepository<OrderItem,Integer>{
	//通过订单查找订单项
	List<OrderItem> findByOrderOrderByIdDesc(Order order);
	//根据产品查找订单项
	List<OrderItem> findByProduct(Product product);
	List<OrderItem> findByUserAndOrderIsNull(User user);
}
