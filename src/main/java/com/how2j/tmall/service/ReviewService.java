package com.how2j.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.how2j.tmall.dao.ReviewDao;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.Review;

@Service
public class ReviewService {
	@Autowired
	ReviewDao reviewDao;
	@Autowired
	ProductService productService;
	
	public void add(Review review){
		reviewDao.save(review);
	}
	
	public List<Review> list(Product product){
		List<Review> result  = reviewDao.findByProductOrderByIdDesc(product);
		return result;
	}
	
	public int getCount(Product product){
		return reviewDao.countByProduct(product);
	}
}
