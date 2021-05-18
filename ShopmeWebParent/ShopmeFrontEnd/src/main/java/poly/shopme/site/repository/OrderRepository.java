package poly.shopme.site.repository;

import org.springframework.data.repository.CrudRepository;

import poly.shopme.common.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {

}
