package poly.shopme.site.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import poly.shopme.common.entity.Order;
import poly.shopme.common.entity.OrderDetail;
import poly.shopme.common.entity.Product;
import poly.shopme.common.entity.ShoppingCart;
import poly.shopme.site.repository.ProductRepository;
import poly.shopme.site.service.OrderService;

@Controller
@SessionAttributes("cart")
public class ShoppingCartController {
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/view_cart")
	public String showShoppingCart(Model model,
			@ModelAttribute("cart") ShoppingCart cartObject) {
		Map<Product, Integer> items = cartObject.getItems();
		
		Iterator<Product> iterator = items.keySet().iterator();
		
		while (iterator.hasNext()) {
			Product product = iterator.next();
			
			Product productInDB = productRepo.findById(product.getId()).get();
			product.setPrice(productInDB.getPrice());
			product.setDiscountPercent(productInDB.getDiscountPercent());
		}
		
		model.addAttribute("pageTitle", "Giỏ hàng");
		
		return "cart/shopping_cart";
	}
	
	@GetMapping("/add_to_cart/{id}")
	public String addToCart(Model model,
			@PathVariable(name = "id") Integer id,
			@ModelAttribute("cart") ShoppingCart cartObject) {
		
		Product product = productRepo.findById(id).get();
		cartObject.addItem(product);
		
		return "redirect:/view_cart";
	}
	
	@GetMapping("/plus_one/{id}")
	public String plusOneQuantity(Model model,
			@PathVariable(name = "id") Integer id,
			@ModelAttribute("cart") ShoppingCart cartObject) {
		
		Product product = productRepo.findById(id).get();
		cartObject.plusOneQuantity(product);
		
		return "redirect:/view_cart";
	}
	
	@GetMapping("/minus_one/{id}")
	public String minusOneQuantity(Model model,
			@PathVariable(name = "id") Integer id,
			@ModelAttribute("cart") ShoppingCart cartObject) {
		
		Product product = productRepo.findById(id).get();
		cartObject.minusOneQuantity(product);
		
		return "redirect:/view_cart";
	}
	
	@GetMapping("/remove_item/{id}")
	public String removeItem(Model model,
			@PathVariable(name = "id") Integer id,
			@ModelAttribute("cart") ShoppingCart cartObject) {
		
		Product product = productRepo.findById(id).get();
		cartObject.removeItem(product);
		
		return "redirect:/view_cart";
	}
	
	@GetMapping("/check_out")
	public String viewCheckOutModal(Model model,
			@ModelAttribute("cart") ShoppingCart cartObject) {
		
		Map<Product, Integer> items = cartObject.getItems();
		
		Iterator<Product> iterator = items.keySet().iterator();
		
		while (iterator.hasNext()) {
			Product product = iterator.next();
			
			Product productInDB = productRepo.findById(product.getId()).get();
			product.setPrice(productInDB.getPrice());
			product.setDiscountPercent(productInDB.getDiscountPercent());
		}
		
		Order order = new Order();
		model.addAttribute("order", order);
		
		return "cart/checkout_modal";
	}
	
	@PostMapping("/orders/create")
	public String createOrder(Order order,
			@ModelAttribute("cart") ShoppingCart cartObject,
			Model model) {
		Map<Product, Integer> items = cartObject.getItems();
		
		Iterator<Product> iterator = items.keySet().iterator();
		
		Set<OrderDetail> orderDetails = new HashSet<>();
		
		while (iterator.hasNext()) {
			Product product = iterator.next();
			
			Integer quantity = items.get(product);
			int subTotal = quantity * product.getDiscountPrice();
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setProduct(product);
			orderDetail.setOrder(order);
			orderDetail.setQuantity(quantity);
			orderDetail.setPrice(product.getDiscountPrice());
			orderDetail.setSubTotal(subTotal);
			
			orderDetails.add(orderDetail);
		}
		
		order.setOrderDetails(orderDetails);
		order.setTotal(cartObject.getTotalAmount());
		
		orderService.create(order);
		
		model.addAttribute("order", order);
		
		cartObject.clear();
		
		return "cart/checkout_success";
	}
	
	@ModelAttribute("cart")
	public ShoppingCart ShoppingCart() {
	    return new ShoppingCart();
	}
}
