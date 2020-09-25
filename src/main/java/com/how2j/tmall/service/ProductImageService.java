package com.how2j.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.how2j.tmall.dao.ProductImageDao;
import com.how2j.tmall.pojo.OrderItem;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.ProductImage;
@Service
public class ProductImageService {
	public static final String type_single="single";
	public static final String type_detail = "detail";
	
	@Autowired
	ProductImageDao productImageDao;
	@Autowired
	ProductService productService;
	
	public void add(ProductImage bean){
		productImageDao.save(bean);
	}
	
	public void delete(int id){
		productImageDao.delete(id);
	}
	
	public ProductImage get(int id){
		return productImageDao.findOne(id);
	}
	
	
	//通过产品查询产品图片
    public List<ProductImage> listSingleProductImages(Product product) {
        return productImageDao.findByProductAndTypeOrderByIdDesc(product, type_single);
    }
    
    public List<ProductImage> listDetailProductImages(Product product) {
        return productImageDao.findByProductAndTypeOrderByIdDesc(product, type_detail);
    }
    
    
    public void setFirstProdutImage(Product product) {
        List<ProductImage> singleImages = listSingleProductImages(product);
        if(!singleImages.isEmpty())
            product.setFirstProductImage(singleImages.get(singleImages.size()-1));
        else
            product.setFirstProductImage(new ProductImage()); //这样做是考虑到产品还没有来得及设置图片，但是在订单后台管理里查看订单项的对应产品图片。
 
    }
    public void setFirstProdutImages(List<Product> products) {
        for (Product product : products)
            setFirstProdutImage(product);
    }
    
    //通过订单项 设置订单项对应产品的第一张图
    public void setFirstProdutImagesOnOrderItems(List<OrderItem> ois) {
        for (OrderItem orderItem : ois) {
            setFirstProdutImage(orderItem.getProduct());
        }
    }
    
}
