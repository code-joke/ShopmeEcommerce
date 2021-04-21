package poly.shopme.site;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import poly.shopme.common.entity.Category;
import poly.shopme.site.service.CategoryService;

@Controller
public class MainController {
	
	@Autowired CategoryService categoryService;
	
	@GetMapping("")
	public String viewHomePage(Model model) {
		List<Category> listCategories = categoryService.listRootCategories();
		model.addAttribute("listCategories", listCategories);
		
		return "index";
	}
}
