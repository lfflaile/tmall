package com.how2j.tmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.Review;

public interface ReviewDao extends JpaRepository<Review,Integer>{
	//返回某产品的评价集合
    List<Review> findByProductOrderByIdDesc(Product product);
    //返回某产品的评价数量
    int countByProduct(Product product);
}
