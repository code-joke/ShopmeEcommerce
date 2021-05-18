package poly.shopme.common.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// ShoppingCart POJO

public class ShoppingCart {
	private Map<Product, Integer> cart = new HashMap<>();
	
	public void addItem(Product product) {
		if(cart.containsKey(product)) {
			Integer quantity = cart.get(product) + 1;
			if(quantity < 10) cart.put(product, quantity);
		} else {
			cart.put(product, 1);
		}
	}
	
	public void plusOneQuantity(Product product) {
		Integer quantity = cart.get(product) + 1;
		if(quantity < 10) cart.put(product, quantity);
	}
	
	public void minusOneQuantity(Product product) {
		Integer quantity = cart.get(product) - 1;
		if(quantity > 0) cart.put(product, quantity);
	}
	
	public void removeItem(Product product) {
		cart.remove(product);
	}
	
	public Map<Product, Integer> getItems() {
		return this.cart;
	}
	
	public int getTotalQuantity() {
		int total = 0;
		
		Iterator<Product> iterator = cart.keySet().iterator();
		
		while (iterator.hasNext()) {
			Product next = iterator.next();
			Integer quantity = cart.get(next);
			total += quantity;
		}
		
		return total;
	}
	
	public int getTotalAmount() {
		int total = 0;
		
		Iterator<Product> iterator = cart.keySet().iterator();
		
		while (iterator.hasNext()) {
			Product product = iterator.next();
			Integer quantity = cart.get(product);
			int subTotal = quantity * product.getDiscountPrice();
			total += subTotal;
		}
		
		return total;
	}
	
	public int getTotalDiscountAmount() {
		int total = 0;
		
		Iterator<Product> iterator = cart.keySet().iterator();
		
		while (iterator.hasNext()) {
			Product product = iterator.next();
			Integer quantity = cart.get(product);
			int subTotal = quantity * product.getDiscountPercent();
			total += subTotal;
		}
		
		return total;
	}
	
	public void clear() {
		cart.clear();
	}
	
	public int getTotalItems() {
		return cart.size();
	}
}
