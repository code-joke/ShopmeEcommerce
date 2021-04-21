package poly.shopme.site.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import poly.shopme.common.entity.Product;
import poly.shopme.site.repository.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTests {
	
	@Autowired
	ProductRepository repo;
	
	@Test
	public void testFindByAlias() {
		String alias = "microsoft-surface-pro-7-core-i5-1035g4";
		Product product = repo.findByAlias(alias);
		
		assertThat(product).isNotNull();
	}
}
