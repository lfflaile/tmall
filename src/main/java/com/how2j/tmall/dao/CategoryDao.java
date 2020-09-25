package com.how2j.tmall.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.how2j.tmall.pojo.Category;

public interface CategoryDao extends JpaRepository<Category,Integer>{

}
