package poly.shopme.site.test.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import poly.shopme.common.entity.Brand;
import poly.shopme.site.repository.BrandRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BrandRepositoryTests {
	
	@Autowired
	private BrandRepository brandRepo;
	
	@Test
	public void testListBrandByCategory() {
		List<Brand> listBrandByCategory = brandRepo.listBrandByCategory(1);
		listBrandByCategory.forEach(brand -> {
			System.out.print(brand.getId());
		});
	}
}
