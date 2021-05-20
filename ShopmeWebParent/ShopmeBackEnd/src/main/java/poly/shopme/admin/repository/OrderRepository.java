package poly.shopme.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import poly.shopme.common.entity.Brand;
import poly.shopme.common.entity.Order;
import poly.shopme.common.entity.Product;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
	
	@Query("SELECT o FROM Order o WHERE o.customerName LIKE %?1% "
			+ "OR o.orderTime LIKE %?1% "
			+ "OR o.total LIKE %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE Order o SET o.total = ?2 WHERE o.id = ?1")
	@Modifying
	public void updateTotal(Integer id, Integer total);
	
	@Query("UPDATE Order o SET o.phone = ?2, o.email = ?3, o.address = ?4, o.note = ?5 WHERE o.id = ?1")
	@Modifying
	public void updateInfoCustomer(Integer id, String phone, String email, String address, String note);
}
