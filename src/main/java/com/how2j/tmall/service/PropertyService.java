package com.how2j.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2j.tmall.dao.PropertyDao;
import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.Property;
import com.how2j.tmall.util.Page4Navigator;

@Service
public class PropertyService {
	@Autowired
	PropertyDao propertyDao;
	@Autowired
	CategoryService categoryService;
	
	public void add(Property bean){
		propertyDao.save(bean);
	}
	
	public void delete(int id){
		propertyDao.delete(id);
	}
	
	public Property get(int id){
		return propertyDao.findOne(id);
	}
	
	public void update(Property bean){
		propertyDao.save(bean);
	}
	
	public Page4Navigator<Property> list(int cid,int start,int size,int navigatePages){
			Category category = categoryService.get(cid);
			Sort sort = new Sort(Sort.Direction.ASC,"id");
			Pageable pageable = new PageRequest(start,size,sort);
			Page<Property> pageFromJPA = propertyDao.findByCategory(category,pageable);
			return new Page4Navigator<>(pageFromJPA, navigatePages);
	}
	
	public List<Property> listByCategory(Category category){
		return propertyDao.findByCategory(category);
	}
}
