package com.ute.shopping.cartitem;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.common.entity.Customer;
import com.ute.common.exception.CustomerNotFoundException;
import com.ute.shopping.customer.ICustomerService;
import com.ute.shopping.util.Utility;

@RestController
@RequestMapping("/api")
public class CartItemRestController {

	@Autowired
	private ICartItemService cartService;
	@Autowired
	private ICustomerService customerService;

	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {

		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);

			return updatedQuantity + " item(s) of this product were added to your shopping cart.";
		} catch (CustomerNotFoundException ex) {
			return "You must login to add this product to cart.";
		}

	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}

		return customerService.findCustomerByEmail(email).get();
	}

	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subtotal = cartService.updateQuantity(productId, quantity, customer);

			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException ex) {
			return "You must login to change quantity of product.";
		}
	}

	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer);

			return "The product has been removed from your shopping cart.";

		} catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}
	}
}