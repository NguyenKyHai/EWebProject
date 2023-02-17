package com.ute.shopping.cartitem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.CartItem;
import com.ute.common.entity.Customer;
import com.ute.common.entity.Product;
import com.ute.shopping.product.IProductRepository;

@Service
public class CartItemService implements ICartItemService{
	
	@Autowired 
	private ICartItemRepository cartRepository;
	@Autowired 
	private IProductRepository productRepository;

	@Override
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) {
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);
		
		CartItem cartItem = cartRepository.findByCustomerAndProduct(customer, product);
		
		if (cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		
		cartItem.setQuantity(updatedQuantity);
		
		cartRepository.save(cartItem);
		
		return updatedQuantity;
	}

	@Override
	public List<CartItem> listCartItems(Customer customer) {
		return cartRepository.findByCustomer(customer);
	}

	@Override
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		cartRepository.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepository.findById(productId).get();
		float subtotal = product.getDiscountPrice() * quantity;
		return subtotal;
	}
	
	@Override
	public void removeProduct(Integer productId, Customer customer) {
		cartRepository.deleteByCustomerAndProduct(customer.getId(), productId);
	}

	@Override
	public void deleteByCustomer(Customer customer) {
		cartRepository.deleteByCustomer(customer.getId());
	}

}
