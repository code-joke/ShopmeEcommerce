package poly.shopme.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import poly.shopme.common.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	
	@Query("UPDATE OrderDetail o SET o.quantity = ?2, o.subTotal = ?3 WHERE o.id = ?1")
	@Modifying
	public void updateQuantity(Integer id, Integer quantity, Integer subTotal);
	
	public Long countById(Integer id);
}
