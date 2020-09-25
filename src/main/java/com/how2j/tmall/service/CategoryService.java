package com.how2j.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2j.tmall.dao.CategoryDao;
import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.util.Page4Navigator;

@Service
public class CategoryService {
	@Autowired
	CategoryDao categoryDao;
	
	public Page4Navigator<Category> list(int start,int size,int navigatePages){
		//根据ID倒叙
		Sort sort = new Sort(Sort.Direction.ASC,"id");
		Pageable pageable = new PageRequest(start,size,sort);
		Page pageFromJPA = categoryDao.findAll(pageable);
		return new Page4Navigator<>(pageFromJPA,navigatePages);
	} 
	
	public List<Category> list(){
		Sort sort = new Sort(Sort.Direction.DESC,"id");
		return categoryDao.findAll(sort);
	}
	
	
    public void add(Category bean) {
        categoryDao.save(bean);
    }
    
    public void delete(int id){
    	categoryDao.delete(id);
    }
    
    public Category get(int id) {
        Category c= categoryDao.findOne(id);
        return c;
    }
    
    public void update(Category bean) {
        categoryDao.save(bean);
    }
    
    public void removeCategoryFromProduct(List<Category> cs){
    	for(Category category : cs){
    		removeCategoryFromProduct(category);
    	}
    }
    
    public void removeCategoryFromProduct(Category category){
    	List<Product> products =category.getProducts();
    	if(null!=products){
    		for(Product product:products){
    			product.setCategory(null);
    		}
    	}
    	List<List<Product>> productsByRowList = category.getProductsByRow();
    	if(null!=productsByRowList){
    		for(List<Product>ps:productsByRowList){
    			for(Product p:ps){
    				p.setCategory(null);
    			}
    		}
    	}
    }
    
    

}
