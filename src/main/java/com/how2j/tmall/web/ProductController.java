package com.how2j.tmall.web;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.service.CategoryService;
import com.how2j.tmall.service.ProductImageService;
import com.how2j.tmall.service.ProductService;
import com.how2j.tmall.util.Page4Navigator;

@RestController
public class ProductController {
	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired 
	ProductImageService productImageService;
	
	@GetMapping("/categories/{cid}/products")
    public Page4Navigator<Product> list(@PathVariable("cid") int cid, @RequestParam(value = "start", defaultValue = "0") int start,@RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Page4Navigator<Product> page =productService.list(cid, start, size,5 );
        productImageService.setFirstProdutImages(page.getContent());
        return page;
    }
	
	@GetMapping("/products/{id}")
	public Product get(@PathVariable("id") int id){
		Product bean = productService.get(id);
		return bean;
	}
	
	@PostMapping("/products")
	public Object add(@RequestBody Product bean){
		bean.setCreateDate(new Date());
		productService.add(bean);
		return bean;
	}
	
	@DeleteMapping("/products/{id}")
	public String delete(@PathVariable("id") int id){
		productService.delete(id);
		return null;
	}
	
	@PutMapping("/products")
	public Object update(@RequestBody Product bean){
		productService.update(bean);
		return bean;
	}
}
