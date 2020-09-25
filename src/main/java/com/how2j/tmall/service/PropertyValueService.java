package com.how2j.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.how2j.tmall.dao.PropertyValueDao;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.Property;
import com.how2j.tmall.pojo.PropertyValue;

@Service
public class PropertyValueService {
	@Autowired
	PropertyValueDao propertyValueDao;
	@Autowired
	PropertyService propertyService;
	
	public void update(PropertyValue bean){
		propertyValueDao.save(bean);
	}
	
	public void init(Product product){
		//通过产品获取分类,然后通过propertyService获取这个分类的属性.
		List<Property> properties = propertyService.listByCategory(product.getCategory());
		for(Property property:properties){
			PropertyValue propertyValue = getByPropertyAndProduct(product, property);
			if(null==propertyValue){
				 propertyValue = new PropertyValue();
				 propertyValue.setProduct(product);
				 propertyValue.setProperty(property);
				 propertyValueDao.save(propertyValue);
			}
		}
	}
	
    public PropertyValue getByPropertyAndProduct(Product product, Property property) {
    	//通过产品和属性获取属性值
        return propertyValueDao.getByPropertyAndProduct(property,product);
    }
    
    public List<PropertyValue> list(Product product) {
        return propertyValueDao.findByProductOrderByIdDesc(product);
    }
}
