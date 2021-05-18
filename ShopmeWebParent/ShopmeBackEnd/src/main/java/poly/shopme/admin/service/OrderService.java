package poly.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poly.shopme.admin.exception.OrderNotFoundException;
import poly.shopme.admin.exception.UserNotFoundException;
import poly.shopme.admin.repository.OrderRepository;
import poly.shopme.common.entity.Order;
import poly.shopme.common.entity.User;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderRepository repo;
	
	public List<Order> listAll() {
		return (List<Order>) repo.findAll();
	}
	
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Không tìm thấy đơn hàng nào với ID: " + id + " !");
		}
	}
}
