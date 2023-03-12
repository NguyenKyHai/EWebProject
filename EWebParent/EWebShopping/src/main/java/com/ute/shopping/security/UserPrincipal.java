package com.ute.shopping.security;

import com.ute.common.constants.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.ute.common.entity.Customer;
import java.util.Collection;
import java.util.Map;

public class UserPrincipal implements OAuth2User, UserDetails {
 
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String email;
    private String password;

    private String status;
    private Map<String, Object> attributes;

    public UserPrincipal(Integer id, String email, String password, String status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.status=status;
    }

    public static UserPrincipal create(Customer customer) {
        return new UserPrincipal(
                customer.getId(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getStatus()
        );
    }

    public static UserPrincipal create(Customer customer, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(customer);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if(this.getStatus().equals(Constants.STATUS_BLOCKED))
            return false;
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
