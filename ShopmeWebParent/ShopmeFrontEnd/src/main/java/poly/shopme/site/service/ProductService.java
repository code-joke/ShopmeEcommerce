package poly.shopme.site.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import poly.shopme.common.entity.Product;
import poly.shopme.common.exception.ProductNotFoundException;
import poly.shopme.site.repository.ProductRepository;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 12;
	public static final int SEARCH_RESULTS_PER_PAGE = 8;
	
	@Autowired
	private ProductRepository repo;
	
	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		String categoryIdMath = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		
		return repo.listByCategory(categoryId, categoryIdMath, pageable);
	}
	
	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product = repo.findByAlias(alias);
		if (product == null) {
			throw new ProductNotFoundException("Không tìm thấy sản phẩm nào với đường dẫn " + alias);
		}
		
		return product;
	}
	
	public Page<Product> search(String keyword, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
		return repo.search(keyword, pageable);
	}
}
