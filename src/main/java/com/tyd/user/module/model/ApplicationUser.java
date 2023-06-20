package com.tyd.user.module.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApplicationUser implements UserDetails {

    private final User user;

    public ApplicationUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthorities(user.getUserRoles());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<UserRole> userRoles) {
        return getGrantedAuthorities(getPrivileges(userRoles));
    }

    private List<String> getPrivileges(Collection<UserRole> roles) {

        List<String> privileges = new ArrayList<>();
        List<UserRolePrivilege> collection = new ArrayList<>();
        for (UserRole role : roles) {
            privileges.add(role.getUserRoleName());
            collection.addAll(role.getUserRolePrivileges());
        }
        for (UserRolePrivilege item : collection) {
            privileges.add(item.getUserRolePrivilegeName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword().getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isUserAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isUserAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isUserCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isUserAccountEnabled();
    }
}
