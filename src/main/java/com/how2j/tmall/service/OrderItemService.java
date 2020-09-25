package com.how2j.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.how2j.tmall.dao.OrderItemDao;
import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.pojo.OrderItem;
import com.how2j.tmall.pojo.Product;
import com.how2j.tmall.pojo.User;

@Service
public class OrderItemService {
	@Autowired
	OrderItemDao orderItemDao;
	@Autowired
	ProductImageService productImageService;
	
	public void fill(List<Order> orders){
			for(Order order:orders){
				fill(order);
			}
	}
	
    public void update(OrderItem orderItem) {
        orderItemDao.save(orderItem);
    }
    
    public void add(OrderItem orderItem) {
        orderItemDao.save(orderItem);
    }
	
	public void fill(Order order){
		//通过订单查找到订单项
		List<OrderItem> orderItems = listByOrder(order);
		//通过订单项确认总金额
		float total = 0;
		//总数
		int totalNumber = 0;
		
		for(OrderItem oi:orderItems){
			total+=oi.getNumber()+oi.getProduct().getPromotePrice();
			totalNumber+=oi.getNumber();
			productImageService.setFirstProdutImage(oi.getProduct());
		}
		order.setTotal(total);
		order.setTotalNumber(totalNumber);
		order.setOrderItems(orderItems);
	}
	
    public List<OrderItem> listByOrder(Order order) {
        return orderItemDao.findByOrderOrderByIdDesc(order);
    }
    
    public int getSaleCount(Product product) {
    	//根据产品找到所有订单项
        List<OrderItem> ois =listByProduct(product);
        int result =0;
        //取出订单项
        for (OrderItem oi : ois) {
        	//确保订单不为空
            if(null!=oi.getOrder())
            	//确保订单以及订单支付时间不为空
                if(null!= oi.getOrder() && null!=oi.getOrder().getPayDate())
                    result+=oi.getNumber();
        }
        return result;
    }
    
    public List<OrderItem> listByProduct(Product product) {
        return orderItemDao.findByProduct(product);
    }
    
    public List<OrderItem> listByUser(User user) {
        return orderItemDao.findByUserAndOrderIsNull(user);
    }
    
	public OrderItem get(int id) {
		return orderItemDao.findOne(id);
	}
	
	public void delete(int id) {
		orderItemDao.delete(id);
	}

}
