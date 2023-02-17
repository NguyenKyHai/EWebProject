package com.ute.shopping.cartitem;

import java.util.List;

import com.ute.common.entity.CartItem;
import com.ute.common.entity.Customer;

public interface ICartItemService {
	Integer addProduct(Integer productId, Integer quantity, Customer customer) ;
	List<CartItem> listCartItems(Customer customer);
	float updateQuantity(Integer productId, Integer quantity, Customer customer);
	void removeProduct(Integer productId, Customer customer);
	void deleteByCustomer(Customer customer);
}
