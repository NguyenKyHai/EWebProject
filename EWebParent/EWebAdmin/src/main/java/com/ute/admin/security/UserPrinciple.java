package com.ute.admin.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ute.common.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {
 
	private static final long serialVersionUID = 1L;
	
	private Integer id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String photos;
    private Collection<? extends GrantedAuthority> roles;

    public UserPrinciple() {
    }

    public UserPrinciple(Integer id, String name, String email, String password, String photos, Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photos = photos;
        this.roles = roles;
    }
    public static UserPrinciple build(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role->
                new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new UserPrinciple(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhotos(),
                authorities
        );
    }
    
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public Collection<? extends GrantedAuthority> getRoles() {
		return roles;
	}

	public void setRoles(Collection<? extends GrantedAuthority> roles) {
		this.roles = roles;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
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
}