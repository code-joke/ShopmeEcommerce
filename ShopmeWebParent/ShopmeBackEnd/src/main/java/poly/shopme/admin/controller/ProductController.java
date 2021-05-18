package poly.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.shopme.admin.exception.ProductNotFoundException;
import poly.shopme.admin.security.ShopmeUserDetails;
import poly.shopme.admin.service.BrandService;
import poly.shopme.admin.service.CategoryService;
import poly.shopme.admin.service.ProductService;
import poly.shopme.admin.utils.FileUploadUtil;
import poly.shopme.admin.utils.ProductSaveHelper;
import poly.shopme.common.entity.Brand;
import poly.shopme.common.entity.Category;
import poly.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null, 0);
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, 
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("categoryId") Integer categoryId) {
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
		List<Product> listProducts = page.getContent();
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		if(categoryId != null) model.addAttribute("categoryId", categoryId);
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listProducts",listProducts);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listCategories", listCategories);
		
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("numberOfExistingExtraImages", 0);
		model.addAttribute("pageTitle", "Tạo mới sản phẩm");
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser) throws IOException{
		
		if(loggedUser.hasRole("Bán hàng")) {
			productService.saveProductPrice(product);
			redirectAttributes.addFlashAttribute("message", "Lưu thành công");
			
			return "redirect:/products";
		}
		
		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageName(imageIDs, imageNames, product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);
		
		Product savedProduct = productService.save(product);
		
		ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
		
		redirectAttributes.addFlashAttribute("message", "Lưu thành công");
		
		return "redirect:/products";
	}
		
	

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		try {
			productService.get(id);
			
			productService.updateProductEnabledStatus(id, enabled);
			String status = enabled ? "Kích hoạt" : "Hủy kích hoạt";
			String message = status + " thành công";
			redirectAttributes.addFlashAttribute("message", message);
			
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		}
			
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;
			
			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);
			
			redirectAttributes.addFlashAttribute("message", "Xóa thành công");
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		}
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();

			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Sửa sản phẩm (ID: " + id +")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			
			return "products/product_form";
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.get(id);
			
			model.addAttribute("product", product);
			
			return "products/product_detail_modal";
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/products";
		}
	}
	
}
