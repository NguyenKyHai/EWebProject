package com.ute.shopping.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class Utility {
	
	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		if (principal == null) return null;
		
		String customerEmail = null;
		
		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		}
//		} else if (principal instanceof OAuth2AuthenticationToken) {
//			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
//			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
//			customerEmail = oauth2User.getEmail();
//		}
		
		return customerEmail;
	}	
}
