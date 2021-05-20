package poly.shopme.admin.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poly.shopme.admin.exception.OrderDetailNotFoundException;
import poly.shopme.admin.exception.OrderNotFoundException;
import poly.shopme.admin.exception.ProductNotFoundException;
import poly.shopme.admin.repository.OrderDetailRepository;
import poly.shopme.admin.repository.OrderRepository;
import poly.shopme.admin.service.OrderDetailService;
import poly.shopme.admin.service.OrderService;
import poly.shopme.admin.service.ProductService;
import poly.shopme.admin.service.UserService;
import poly.shopme.common.entity.Order;
import poly.shopme.common.entity.OrderDetail;
import poly.shopme.common.entity.Product;
import poly.shopme.common.entity.User;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderDetailService orderDetailService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private OrderDetailRepository orderDetailRepo;
	
	@GetMapping("/orders")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "id", "desc", null);
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, 
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<Order> page = orderService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Order> listOrders = page.getContent();
		
		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "orders/orders";
	}
			
	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Order order = orderService.get(id);
			
			Iterator<OrderDetail> iterator = order.getOrderDetails().iterator();
			
			List<String> warningList = new ArrayList<>();
			while (iterator.hasNext()) {
				OrderDetail next = iterator.next();
				Integer quantity = next.getQuantity();
				Integer quantityInDB = next.getProduct().getQuantityInStock();
				if(quantity > quantityInDB) {
					warningList.add("Số lượng hàng trong kho không đủ: " + next.getProduct().getName());
				}
			}
			
			model.addAttribute("warningList", warningList);
			
			model.addAttribute("order", order);
			model.addAttribute("pageTitle", "Sửa đơn hàng (ID: " + id +")");
			
			return "orders/order_form";
		} catch (OrderNotFoundException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/orders";
		}
	}
	
	@GetMapping("/orders/plus_one/{orderId}/{orderDetailId}")
	public String plusOneQuantity(Model model,
			@PathVariable(name = "orderId") Integer orderId,
			@PathVariable(name = "orderDetailId") Integer orderDetailId) {
		
		OrderDetail detail = orderDetailRepo.findById(orderDetailId).get();
		Integer newQty =  detail.getQuantity() + 1;
		Integer newSubtotal = newQty * detail.getPrice();
		orderDetailService.updateQuantityProduct(orderDetailId, newQty, newSubtotal);
		
		Order order = orderRepo.findById(orderId).get();
		Integer newTotal = order.getTotal() + detail.getPrice();
		orderService.updateTotal(orderId, newTotal);
		
		return "redirect:/orders/edit/" + orderId;
	}
	
	@GetMapping("/orders/minus_one/{orderId}/{orderDetailId}")
	public String minusOneQuantity(Model model,
			@PathVariable(name = "orderId") Integer orderId,
			@PathVariable(name = "orderDetailId") Integer orderDetailId) {
		
		OrderDetail detail = orderDetailRepo.findById(orderDetailId).get();
		Integer newQty =  detail.getQuantity() - 1;
		Integer newSubtotal = newQty * detail.getPrice();
		orderDetailService.updateQuantityProduct(orderDetailId, newQty, newSubtotal);
		
		Order order = orderRepo.findById(orderId).get();
		Integer newTotal = order.getTotal() - detail.getPrice();
		orderService.updateTotal(orderId, newTotal);
		
		return "redirect:/orders/edit/" + orderId;
	}
	
	@GetMapping("/orders/remove_item/{orderId}/{orderDetailId}")
	public String removeItem(Model model,
			@PathVariable(name = "orderId") Integer orderId,
			@PathVariable(name = "orderDetailId") Integer orderDetailId,
			RedirectAttributes redirectAttributes) {
		try {
			OrderDetail item = orderDetailRepo.findById(orderDetailId).get();
			Integer subtotal = item.getSubTotal();
			
			Order order = orderRepo.findById(orderId).get();
			Integer newTotal = order.getTotal() - subtotal;
			orderService.updateTotal(orderId, newTotal);
			
			orderDetailService.delete(orderDetailId);
			
			redirectAttributes.addFlashAttribute("message", "Xóa thành công");
		}catch (OrderDetailNotFoundException ex) {
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		}
		
		return "redirect:/orders/edit/" + orderId;
	}
	
	@GetMapping("/orders/add_item/{orderId}")
	public String showAddProductModal(Model model,
			@PathVariable(name = "orderId") Integer orderId) {
		
		Order order = orderRepo.findById(orderId).get();

		List<Product> listProducts = productService.listAll();
		
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("order", order);

		return "orders/product_modal";
	}
	
	@GetMapping("/order/accept/{orderId}")
	public String setOrderVerified(Model model,
			@PathVariable(name = "orderId") Integer orderId) {
		
		Order order = orderRepo.findById(orderId).get();
		order.setStatus("Đã xác nhận");
		
		orderRepo.save(order);
		
		return "redirect:/orders/edit/" + orderId;
	}
	
	@PostMapping("/order/updateInfoCustomer/{orderId}")
	public String saveOrder(
			@PathVariable(name = "orderId") Integer orderId,
			@RequestParam(name = "phone", required = false) String phone,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "address", required = false) String address,
			@RequestParam(name = "note") String note,
			RedirectAttributes redirectAttributes) {
		
		orderService.updateInfoCustomer(orderId, phone, email, address, note);
		
		redirectAttributes.addFlashAttribute("message", "Đã cập nhật thông tin giao hàng");
		
		return "redirect:/orders/edit/" + orderId;
	}
	
	
	@PostMapping("/orders/add_product_to_order")
	public String addProductToOrder(
			@RequestParam(name = "orderId") Integer orderId,
			@RequestParam(name = "productId") Integer productId,
			@RequestParam(name = "quantity") Integer quantity) throws ProductNotFoundException, OrderNotFoundException {
		
		if(productId == 0) return "redirect:/orders/edit/" + orderId;

		Order order = orderService.get(orderId);
		
		Product product = productService.get(productId);
		
		Integer subtotal = product.getDiscountPrice() * quantity;
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setQuantity(quantity);
		orderDetail.setSubTotal(subtotal);
		orderDetail.setOrder(order);
		orderDetail.setPrice(product.getDiscountPrice());
		
		Integer newtotal = order.getTotal() + subtotal;
		order.setTotal(newtotal);
		
		orderDetailRepo.save(orderDetail);
		orderRepo.save(order);
		
		return "redirect:/orders/edit/" + orderId;
	}
	
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Order order = orderService.get(id);
			
			model.addAttribute("order", order);
			
			return "orders/order_detail_modal";
		} catch (OrderNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/orders";
		}
	}
	
	@GetMapping("/orders/edit_shippingCost/{orderId}")
	public String showEditShippingCostModal(Model model,
			@PathVariable(name = "orderId") Integer orderId) {
		
		Order order = orderRepo.findById(orderId).get();

		model.addAttribute("order", order);

		return "orders/shipping_cost_modal";
	}
	
	@GetMapping("/orders/edit_discountTotal/{orderId}")
	public String showEditDiscountTotalModal(Model model,
			@PathVariable(name = "orderId") Integer orderId) {
		
		Order order = orderRepo.findById(orderId).get();

		model.addAttribute("order", order);

		return "orders/discount_total_modal";
	}
	
}
