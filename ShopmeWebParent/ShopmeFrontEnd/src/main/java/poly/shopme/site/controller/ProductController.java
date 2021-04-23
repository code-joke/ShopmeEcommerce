package poly.shopme.site.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import poly.shopme.common.entity.Brand;
import poly.shopme.common.entity.Category;
import poly.shopme.common.entity.Product;
import poly.shopme.common.exception.CategoryNotFoundException;
import poly.shopme.common.exception.ProductNotFoundException;
import poly.shopme.site.service.CategoryService;
import poly.shopme.site.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
			Model model) {
		return viewCategoryByPage(alias, 1, model);
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias,
			@PathVariable("pageNum") int pageNum,
			Model model) {
		try {
			Category category = categoryService.getCategory(alias);
			
			List<Brand> listBrandByCategory = categoryService.listByBrand(category.getId());
			
			List<Category> listCategoryParents =  categoryService.getCategoryParents(category);
			
			Page<Product> pageProduct =  productService.listByCategory(pageNum, category.getId());
			
			List<Product> listProducts = pageProduct.getContent();
			
			long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
			
			if(endCount > pageProduct.getTotalElements()) {
				endCount = pageProduct.getTotalElements();
			}
			
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", pageProduct.getTotalPages());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", pageProduct.getTotalElements());
			model.addAttribute("listProducts", listProducts);
			model.addAttribute("pageTitle", category.getName());
			model.addAttribute("category", category);
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("listBrandByCategory", listBrandByCategory);
			
			return "product/product_by_category";
		} catch (CategoryNotFoundException ex) {
			return "error/404";
		}
	}
	
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
		
		try {
			Product product = productService.getProduct(alias);
			List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
			
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getShortName());
			
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
}
