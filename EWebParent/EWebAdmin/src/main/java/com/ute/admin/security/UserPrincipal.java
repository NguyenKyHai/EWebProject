package com.ute.admin.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ute.common.constants.Constants;
import com.ute.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;

    private String status;

    private Collection<? extends GrantedAuthority> roles;

    public UserPrincipal() {
    }

    public UserPrincipal(Integer id, String name, String email, String password, String status, Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.status=status;
        this.roles = roles;
    }
    public static UserPrincipal build(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role->
                new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new UserPrincipal(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus(),
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
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
}