package com.ute.common.request;

import java.util.List;


public class ShoppingCartRequest {

	private List<LineItem> lineItem;
	private ShippingAddressRequest shippingAddressRequest;
	private String paymentMethod;
	private String totalPrice;
	private String note;
	private String customerId;

	public ShoppingCartRequest() {
		super();
	}

	public List<LineItem> getLineItem() {
		return lineItem;
	}

	public void setLineItem(List<LineItem> lineItem) {
		this.lineItem = lineItem;
	}

	public ShippingAddressRequest getShippingAddress() {
		return shippingAddressRequest;
	}

	public void setShippingAddress(ShippingAddressRequest shippingAddressRequest) {
		this.shippingAddressRequest = shippingAddressRequest;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
