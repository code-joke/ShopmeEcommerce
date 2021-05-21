package poly.shopme.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import poly.shopme.common.entity.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
	
	@Query("SELECT o FROM Order o WHERE o.customerName LIKE %?1% "
			+ "OR o.orderTime LIKE %?1% "
			+ "OR o.total LIKE %?1% "
			+ "OR o.status LIKE %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT o FROM Order o WHERE o.status not like '%Đã xác nhận%' and o.status not like '%Chờ xác nhận%'")
	public Page<Order> findAllInShipping(String keyword, Pageable pageable);
	
	@Query(value = "SELECT * FROM orders WHERE (status NOT LIKE '%Đã xác nhận%' AND status NOT LIKE '%Chờ xác nhận%') "
			+ "AND ("
			+ "id LIKE %?1% "
			+ "OR total LIKE %?1% "
			+ "OR status LIKE %?1%)", nativeQuery = true)
	public Page<Order> searchAllInShipping(String keyword, Pageable pageable);
	
	@Query("UPDATE Order o SET o.total = ?2 WHERE o.id = ?1")
	@Modifying
	public void updateTotal(Integer id, Integer total);
	
	@Query("UPDATE Order o SET o.phone = ?2, o.email = ?3, o.address = ?4, o.note = ?5 WHERE o.id = ?1")
	@Modifying
	public void updateInfoCustomer(Integer id, String phone, String email, String address, String note);
	
	public Long countById(Integer id);
}
