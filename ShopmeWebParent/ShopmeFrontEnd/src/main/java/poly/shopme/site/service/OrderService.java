package poly.shopme.site.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.shopme.common.entity.Order;
import poly.shopme.site.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repo;
	
	public Order create(Order order) {
		order.setOrderTime(new Date());
		
		return repo.save(order);
	}
}
