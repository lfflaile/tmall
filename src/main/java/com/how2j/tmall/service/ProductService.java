package com.how2j.tmall.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.how2j.tmall.dao.ProductDao;
import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.util.Page4Navigator;

/**
 * @author Administrator
 *
 */
@Service
public class ProductService {
	@Autowired
	ProductDao productDao;
	@Autowired
	CategoryService categoryService;
	@Autowired 
	ProductImageService productImageService;
	@Autowired
	OrderItemService orderItemService;
	
	@Autowired
	ReviewService reviewService;
	
	public void add(Product bean){
		productDao.save(bean);
	}
	
	public void delete(int id){
		productDao.delete(id);
	}
	
	public Product get(int id){
		return productDao.findOne(id);
	}
	
	public void update(Product bean){
		productDao.save(bean);
	}
	
	public Page4Navigator<Product> list(int cid,int start,int size,int navigatePages){
		//获得category类
		Category category = categoryService.get(cid);
		//升序排列
		Sort sort = new Sort(Sort.Direction.ASC,"id");
		//pageable是起点 规格 排序规则
		Pageable pageable = new PageRequest(start,size,sort);
		//page还加上了分类
		Page<Product> pagefromJPA = productDao.findByCategory(category, pageable);
		//组成最完美的page4Navigator 还加上了分页的页数
		return new Page4Navigator<>(pagefromJPA,navigatePages);
	}
	
	public void fill(Category category){
		List<Product> products =  listByCategory(category);
		//把每件产品的第一张图片都给设置好
        productImageService.setFirstProdutImages(products);
        //再将产品放入分类中
        category.setProducts(products);
	}
	
	public void fill(List<Category> categories){
		for(Category category:categories){
			fill(category);
		}
	}
	
    public void fillByRow(List<Category> categorys) {
        int productNumberEachRow = 8;
        for (Category category : categorys) {
            List<Product> products =  category.getProducts();
            List<List<Product>> productsByRow =  new ArrayList<>();
            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
                int size = i+productNumberEachRow;
                size= size>products.size()?products.size():size;
                List<Product> productsOfEachRow =products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
    }
	
	public List<Product> listByCategory(Category category){
		return productDao.findByCategoryOrderById(category);
	}
	
	//设置销量和评论数
	public void setSaleAndReviewNumber(Product product){
		int saleCount = orderItemService.getSaleCount(product);
		product.setSaleCount(saleCount);
		int reviewCount = reviewService.getCount(product);
		product.setReviewCount(reviewCount);
	}
	
    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products)
            setSaleAndReviewNumber(product);
    }
    
    public List<Product> search(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        List<Product> products =productDao.findByNameLike("%"+keyword+"%",pageable);
        return products;
    }
    
	
	
	
	
	
}
