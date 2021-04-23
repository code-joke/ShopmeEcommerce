package poly.shopme.site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import poly.shopme.common.entity.Brand;

public interface BrandRepository extends CrudRepository<Brand, Integer> {
	
	@Query(value = "SELECT id, name, logo FROM brands JOIN brands_categories on brands.id = brands_categories.brand_id WHERE category_id = ?1", nativeQuery = true)
	public List<Brand> listBrandByCategory(Integer categoryId);
}
