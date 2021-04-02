package poly.shopme.admin.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import poly.shopme.admin.repository.ProductRepository;
import poly.shopme.common.entity.Brand;
import poly.shopme.common.entity.Category;
import poly.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 10);
		Category category = entityManager.find(Category.class, 2);
		
		Product product = new Product();
		product.setName("Dell XPS ");
		product.setAlias("Dell XPS ");
		product.setShortDescription("Short description");
		product.setFullDescription("full description");
		
		product.setEnabled(true);
		product.setInStock(true);
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(4563);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = repo.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllProduct() {
		Iterable<Product> products = repo.findAll();
		
		products.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Integer id = 2;
		Product product = repo.findById(id).get();
		System.out.println(product);
		
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 2;
		Product product = repo.findById(id).get();
		product.setPrice(0);
		
		repo.save(product);
		
		Product updatedProduct = repo.findById(id).get();
		
		assertThat(updatedProduct.getPrice()).isEqualTo(0);
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id = 4;
		repo.deleteById(id);
		
		Optional<Product> result = repo.findById(id);
		
		assertThat(!result.isPresent());
	}
}
