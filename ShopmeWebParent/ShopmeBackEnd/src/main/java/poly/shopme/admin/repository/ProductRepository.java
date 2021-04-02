package poly.shopme.admin.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import poly.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

}
