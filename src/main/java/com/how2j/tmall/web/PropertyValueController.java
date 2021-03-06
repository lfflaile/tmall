package com.how2j.tmall.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.PropertyValue;
import com.how2j.tmall.service.ProductService;
import com.how2j.tmall.service.PropertyValueService;

@RestController
public class PropertyValueController {
	@Autowired
	PropertyValueService propertyValueService;
	@Autowired
	ProductService productSerivce;
	
	
	@GetMapping("/products/{pid}/propertyValues")
	public List<PropertyValue> list(@PathVariable("pid") int pid){
		Product product = productSerivce.get(pid);
		propertyValueService.init(product);
		List<PropertyValue> propertyValues = propertyValueService.list(product);
		return propertyValues;
	}
	
	@PutMapping("/propertyValues")
	public Object update(@RequestBody PropertyValue bean){
		propertyValueService.update(bean);
		return bean;
	}
}
