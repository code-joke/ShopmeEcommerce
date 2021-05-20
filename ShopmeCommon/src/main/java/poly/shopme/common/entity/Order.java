package poly.shopme.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "customer_name", length = 256, nullable = false)
	private String customerName;
	
	@Column(length = 128, nullable = false)
	private String email;
	
	@Column(length = 15, nullable = false)
	private String phone;
	
	@Column(length = 500, nullable = false)
	private String address;
	
	@Column(length = 500)
	private String note;
	
	@Column(name = "shipping_cost")
	private Integer shippingCost;
	
	@Column(name = "discount_total")
	private Integer discountTotal;
	
	@Column(name = "discount_note", length = 256)
	private String discountNote;
	
	@Column
	private int total;
	
	@Column(name = "order_time")
	private Date orderTime;
	
	@Column(length = 20)
	private String status;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderTrack> orderTracks = new HashSet<>();
	
	public Order() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public Integer getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(Integer shippingCost) {
		this.shippingCost = shippingCost;
	}

	public Integer getDiscountTotal() {
		return discountTotal;
	}

	public void setDiscountTotal(Integer discountTotal) {
		this.discountTotal = discountTotal;
	}

	public String getDiscountNote() {
		return discountNote;
	}

	public void setDiscountNote(String discountNote) {
		this.discountNote = discountNote;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}
	
	public Set<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTracks(Set<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	@Transient
	public int getTotalQuantity() {
		int total = 0;
		
		Iterator<OrderDetail> iterator = orderDetails.iterator();
		
		while (iterator.hasNext()) {
			OrderDetail next = iterator.next();
			Integer quantity = next.getQuantity();
			total += quantity;
		}
		
		return total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
