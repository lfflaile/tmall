package com.how2j.tmall.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.how2j.tmall.comparator.ProductAllComparator;
import com.how2j.tmall.comparator.ProductDateComparator;
import com.how2j.tmall.comparator.ProductPriceComparator;
import com.how2j.tmall.comparator.ProductReviewComparator;
import com.how2j.tmall.comparator.ProductSaleCountComparator;
import com.how2j.tmall.pojo.Category;
import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.pojo.OrderItem;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.ProductImage;
import com.how2j.tmall.pojo.PropertyValue;
import com.how2j.tmall.pojo.Review;
import com.how2j.tmall.pojo.User;
import com.how2j.tmall.service.CategoryService;
import com.how2j.tmall.service.OrderItemService;
import com.how2j.tmall.service.OrderService;
import com.how2j.tmall.service.ProductImageService;
import com.how2j.tmall.service.ProductService;
import com.how2j.tmall.service.PropertyValueService;
import com.how2j.tmall.service.ReviewService;
import com.how2j.tmall.service.UserService;
import com.how2j.tmall.util.Result;

@RestController
public class ForeRESTController {
	 @Autowired
	 CategoryService categoryService;
	 @Autowired
	 ProductService productService;
	 @Autowired
	 UserService userService;
	 @Autowired
	 ProductImageService productImageService;
	 @Autowired
	 PropertyValueService propertyValueService;
	 @Autowired
	 ReviewService reviewService;
	 @Autowired
	 OrderItemService orderItemService;
	 @Autowired
	 OrderService orderService;
	 
	 @GetMapping("/forehome")
	 public Object home() {
	     List<Category> cs= categoryService.list();
	     productService.fill(cs);
	     productService.fillByRow(cs);
	     categoryService.removeCategoryFromProduct(cs);
	     return cs;
	 }   
	 
	 @PostMapping("/foreregister")
	 public Object register(@RequestBody User user){
		 String name = user.getName();
		 String password = user.getPassword();
		 //通过HtmlUtils.htmlEscape将用户名里面的特殊字符进行转义
		 name = HtmlUtils.htmlEscape(name);
		 user.setName(name);
		 boolean exist = userService.isExist(name);
		 if(exist){
			 String message = "用户名已存在";
			 return Result.fail(message);
		 }
		 user.setPassword(password);
		 userService.add(user);
		 return Result.success();
	 }
	 
	 @PostMapping("/forelogin")
	 public Object login(@RequestBody User userParam, HttpSession session) {
	     String name =  userParam.getName();
	     name = HtmlUtils.htmlEscape(name);
	  
	     User user =userService.get(name,userParam.getPassword());
	     if(null==user){
	         String message ="账号密码错误";
	         return Result.fail(message);
	     }
	     else{
	         session.setAttribute("user", user);
	         return Result.success();
	     }
	 }
	 
	 @GetMapping("/foreproduct/{pid}")
	 public Object product(@PathVariable("pid") int id){
		 //通过ID获取产品
		 Product product = productService.get(id);
		 //通过产品获取单个产品图
		 List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
		 List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
		 product.setProductSingleImages(productSingleImages);
		 product.setProductDetailImages(productDetailImages);
		 //获取产品属性值
		 List<PropertyValue>pvs = propertyValueService.list(product);
		 //获取产品所有评论
		 List<Review> reviews = reviewService.list(product);
		 //设置产品评论数和销量
		 productService.setSaleAndReviewNumber(product);
		 //设置产品首张照片
		 productImageService.setFirstProdutImage(product);
		 
		 Map<String,Object> map = new HashMap<>();
		 map.put("product",product);
		 map.put("pvs",pvs);
		 map.put("reviews",reviews);
		 return Result.success(map);
	 }
	 
	 @GetMapping("forecheckLogin")
	 public Object checkLogin(HttpSession session){
		 User user = (User)session.getAttribute("user");
		 if(null!=user){
			 return Result.success();
		 }
		 return Result.fail("未登录");
	 }
	 
	 @GetMapping("forecategory/{cid}")
	 public Object category(@PathVariable int cid,String sort){
		 	Category c = categoryService.get(cid);
		 	productService.fill(c);
		 	productService.setSaleAndReviewNumber(c.getProducts());
		 	categoryService.removeCategoryFromProduct(c);
		 	
		 	if(null!=sort){
	            switch(sort){
                case "review":
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;
 
                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;
 
                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;
 
                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
		 }
		 	return c;
	 }
	 
	 @PostMapping("foresearch")
	 public Object search( String keyword){
	     if(null==keyword)
	         keyword = "";
	     List<Product> ps= productService.search(keyword,0,20);
	     productImageService.setFirstProdutImages(ps);
	     productService.setSaleAndReviewNumber(ps);
	     return ps;
	 }
	 
	 @GetMapping("forebuyone")
	 public Object buyone(int pid,int num,HttpSession session){
		 return buyoneAndAddCart(pid,num,session);
	 }
	 
	 private int buyoneAndAddCart(int pid,int num,HttpSession session){
		 Product product = productService.get(pid);
		 int oiid = 0;
		 User user =(User)session.getAttribute("user");
		 boolean found = false;
		 //通过用户获取购物车订单项
		 List<OrderItem> ois = orderItemService.listByUser(user);
		 //如果ID对应上 就代表购物车里有相应的订单 将数量加上去
		 for(OrderItem oi:ois){
			 if(oi.getProduct().getId()==product.getId()){
				 oi.setNumber(oi.getNumber()+num);
				 orderItemService.update(oi);
				 found=true;
				 //订单ID
				 oiid=oi.getId();
			 }
		 }
		 if(!found){
			 OrderItem oi = new OrderItem();
			 oi.setUser(user);
			 oi.setProduct(product);
			 oi.setNumber(num);
			 orderItemService.add(oi);
			 oiid = oi.getId();
		 }
		 return oiid;
	 }
	 
	 @GetMapping("forebuy")
	 public Object buy(String[] oiid,HttpSession session){
		 List<OrderItem> orderItems = new ArrayList<>();
		 float total=0;
		 for(String strid:oiid){
			 int id = Integer.parseInt(strid);
			 OrderItem oi = orderItemService.get(id);
			 //算出总价格
			 total += oi.getProduct().getPromotePrice()*oi.getNumber();
			 orderItems.add(oi);
		 }
		 productImageService.setFirstProdutImagesOnOrderItems(orderItems);
		 session.setAttribute("ois",orderItems);
		 Map<String,Object> map = new HashMap<>();
		 map.put("orderItems",orderItems);
		 map.put("total",total);
		 return Result.success(map);
	 }
	 
	    @GetMapping("foreaddCart")
	    public Object addCart(int pid, int num, HttpSession session) {
	        buyoneAndAddCart(pid,num,session);
	        return Result.success();
	    }
	    
	    @GetMapping("forecart")
	    public Object cart(HttpSession session){
	    	User user = (User)session.getAttribute("user");
	    	List<OrderItem> ois = orderItemService.listByUser(user);
	    	productImageService.setFirstProdutImagesOnOrderItems(ois);
	    	return ois;
	    }
	    
	    @GetMapping("forechangeOrderItem")
	    public Object changeOrderItem(HttpSession session,int pid,int num){
	    	User user = (User)session.getAttribute("user");
	    	if(null==user){
	    		return Result.fail("未登录");
	    	}
	    	List<OrderItem> ois = orderItemService.listByUser(user);
	    	for(OrderItem oi:ois){
	    		if(oi.getProduct().getId()==pid){
	    			oi.setNumber(num);
	    			orderItemService.update(oi);
	    			break;
	    		}
	    	}
	    	return Result.success();
	    }
	    
	    @GetMapping("foredeleteOrderItem")
	    public Object deleteOrderItem(HttpSession session,int oiid){
	    	User user = (User)session.getAttribute("user");
	    	if(null==user){
	    		return Result.fail("未登录");
	    	}
	    	orderItemService.delete(oiid);
	    	return Result.success();
	    }
	    
	    @PostMapping("forecreateOrder")
	    public Object createOrder(@RequestBody Order order,HttpSession session){
	    	User user = (User) session.getAttribute("user");
	    	if(null==user){
	    		return Result.fail("未登录");
	    	}
	    	String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
	    			+RandomUtils.nextInt(10000);
	    	order.setOrdercode(orderCode);
	    	order.setCreateDate(new Date());
	    	order.setUser(user);
	    	order.setStatus(OrderService.waitPay);
	    	List<OrderItem> ois = (List<OrderItem>)  session.getAttribute("ois");
	    	float total = orderService.add(order, ois);
	    	Map<String,Object> map = new HashMap<>();
	    	map.put("oid",order.getId());
	    	map.put("total",total);
	    	return Result.success(map);
	    }
	    
	    @GetMapping("forepayed")
	    public Object payed(int oid){
	    	Order order = orderService.get(oid);
	    	order.setStatus(OrderService.waitDelivery);
	    	order.setPayDate(new Date());
	    	orderService.update(order);
	    	return order;
	    }
	    
	    @GetMapping("forebought")
	    public Object bought(HttpSession session){
	    	User user = (User) session.getAttribute("user");
	    	if(null==user){
	    		return Result.fail("未登录");
	    	}
	        List<Order> os= orderService.listByUserWithoutDelete(user);
	        orderService.removeOrderFromOrderItem(os);
	        return os;
	    }
	    
	    @GetMapping("foreconfirmPay")
	    public Object confirmPay(int oid){
	    	Order o = orderService.get(oid);
	    	orderItemService.fill(o);
	    	orderService.cacl(o);
	    	orderService.removeOrderFromOrderItem(o);
	    	return o;
	    }
	    
	    @GetMapping("foreorderConfirmed")
	    public Object orderConfirmed(int oid){
	    	Order o = orderService.get(oid);
	    	o.setStatus(OrderService.waitReview);
	    	o.setConfirmDate(new Date());
	    	orderService.update(o);
	    	return Result.success();
	    }
	    
	    @PutMapping("foredeleteOrder")
	    public Object deleteOrder(int oid){
	    	Order o = orderService.get(oid);
	    	o.setStatus(OrderService.delete);
	    	orderService.update(o);
	    	return Result.success();
	    }
	    
	    @GetMapping("forereview")
	    public Object review(int oid){
	    	Order o = orderService.get(oid);
	    	orderItemService.fill(o);
	    	orderService.removeOrderFromOrderItem(o);
	    	Product p = o.getOrderItems().get(0).getProduct();
	    	List<Review> reviews = reviewService.list(p);
	    	//设置产品的销量和评论数
	    	productService.setSaleAndReviewNumber(p);
	    	Map<String,Object> map = new HashMap<>();
	    	map.put("p",p);
	        map.put("o", o);
	        map.put("reviews", reviews);
	        return Result.success(map);
	    }
	    
	    @PostMapping("foredoreview")
	    public Object doreview(HttpSession session,int oid,int pid,String content){
	    	Order o = orderService.get(oid);
	    	o.setStatus(OrderService.finish);
	    	orderService.update(o);
	    	Product p = productService.get(pid);
	    	content = HtmlUtils.htmlEscape(content);
	    	User user = (User)session.getAttribute("user");
	    	Review review = new Review();
	    	review.setContent(content);
	    	review.setProduct(p);
	    	review.setCreateDate(new Date());
	    	review.setUser(user);
	    	reviewService.add(review);
	    	return Result.success();
	    }
}
