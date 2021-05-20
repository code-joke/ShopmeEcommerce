package poly.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poly.shopme.admin.exception.OrderNotFoundException;
import poly.shopme.admin.exception.UserNotFoundException;
import poly.shopme.admin.repository.OrderRepository;
import poly.shopme.common.entity.Order;
import poly.shopme.common.entity.Product;

@Service
@Transactional
public class OrderService {
	public static final int ORDERS_PER_PAGE = 10;

	@Autowired
	private OrderRepository repo;
	
	public List<Order> listAll() {
		return (List<Order>) repo.findAll();
	}
	
	public Page<Order> listByPage(int pageNum, String sortField, String sortDir, 
			String keyword) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
		
		if(keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		
		return repo.findAll(pageable);
	}
	
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Không tìm thấy đơn hàng nào với ID: " + id + " !");
		}
	}
	
	public void updateTotal(Integer id, Integer total) {
		repo.updateTotal(id, total);
	}
	
	public void updateInfoCustomer(Integer id, String phone, String email, String address, String note) {
		repo.updateInfoCustomer(id, phone, email, address, note);
	}
	
	public void save(Order order) {
		repo.save(order);
	}
}
