package poly.shopme.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poly.shopme.admin.exception.OrderDetailNotFoundException;
import poly.shopme.admin.exception.UserNotFoundException;
import poly.shopme.admin.repository.OrderDetailRepository;

@Service
@Transactional
public class OrderDetailService {
	
	@Autowired
	private OrderDetailRepository repo;
	
	public void updateQuantityProduct(Integer id, Integer quantity, Integer subTotal) {
		repo.updateQuantity(id, quantity, subTotal);
	}
	
	public void delete(Integer id) throws OrderDetailNotFoundException {
		Long countById = repo.countById(id);
		
		if(countById == null || countById == 0) {
			throw new OrderDetailNotFoundException("Không tìm thấy mục nào với ID: " + id + " !");
		}
		
		repo.deleteById(id);
	}
	
}
