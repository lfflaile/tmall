package com.how2j.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.how2j.tmall.dao.OrderDao;
import com.how2j.tmall.pojo.Order;
import com.how2j.tmall.pojo.OrderItem;
import com.how2j.tmall.pojo.User;
import com.how2j.tmall.util.Page4Navigator;

@Service
public class OrderService {
	public static final String waitPay = "waitPay";
	public static final String waitDelivery = "waitDelivery";
	public static final String waitConfirm = "waitConfirm";
	public static final String waitReview = "waitReview";
	public static final String finish = "finish";
	public static final String delete = "delete";
	
	@Autowired
	OrderDao orderDao;
	@Autowired
	OrderItemService orderItemService;
	
	public Page4Navigator<Order> list(int start,int size,int navigatePages){
		Sort sort = new Sort(Sort.Direction.DESC,"id");
		Pageable pageable = new PageRequest(start, size,sort);
		Page pageFromJPA = orderDao.findAll(pageable);
		return new Page4Navigator<>(pageFromJPA, navigatePages);
	}
	
	public void removeOrderFromOrderItem(List<Order>orders){
		for(Order order:orders){
			removeOrderFromOrderItem(order);
		}
	}
	
	public void removeOrderFromOrderItem(Order order){
		List<OrderItem> orderItems = order.getOrderItems();
		for(OrderItem oi:orderItems){
			oi.setOrder(null);
		}
	}
	
	public Order get(int id){
		return orderDao.findOne(id);
	}
	
	public void update(Order bean){
		orderDao.save(bean);
	}
	
	@Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
	public float add(Order order,List<OrderItem> ois){
		float total=0;
		add(order);
        if(false)
            throw new RuntimeException();
        for (OrderItem oi: ois) {
            oi.setOrder(order);
            orderItemService.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
	}
	
    public void add(Order order) {
        orderDao.save(order);
    }
    
    public List<Order> listByUserWithoutDelete(User user) {
        List<Order> orders = listByUserAndNotDeleted(user);
        //这一步使得订单的金额,数量,以及订单项属性都拥有了值
        orderItemService.fill(orders);
        return orders;
    }
 
    public List<Order> listByUserAndNotDeleted(User user) {
        return orderDao.findByUserAndStatusNotOrderByIdDesc(user,OrderService.delete);
    }
    
    public void cacl(Order o){
    	List<OrderItem> orderItems = o.getOrderItems();
    	float total = 0;
        for (OrderItem orderItem : orderItems) {
            total+=orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
        }
        o.setTotal(total);
    }
	
}
