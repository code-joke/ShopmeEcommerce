package poly.shopme.admin.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import poly.shopme.common.entity.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
	
}
