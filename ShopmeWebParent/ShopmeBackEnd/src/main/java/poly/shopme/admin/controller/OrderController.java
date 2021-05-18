package poly.shopme.admin.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.shopme.admin.exception.OrderNotFoundException;
import poly.shopme.admin.exception.UserNotFoundException;
import poly.shopme.admin.service.OrderService;
import poly.shopme.common.entity.Order;
import poly.shopme.common.entity.OrderDetail;
import poly.shopme.common.entity.Role;
import poly.shopme.common.entity.User;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/orders")
	public String listAll(Model model) {
		List<Order> listOrders = orderService.listAll();
		
		model.addAttribute("listOrders", listOrders);
		
		return "orders/orders";
	}
	
	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Order order = orderService.get(id);
			
			model.addAttribute("order", order);
			model.addAttribute("pageTitle", "Sửa đơn hàng (ID: " + id +")");
			
			return "orders/order_form";
		} catch (OrderNotFoundException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/orders";
		}
	}
}
