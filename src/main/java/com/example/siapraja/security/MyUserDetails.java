package com.example.siapraja.security;

import com.example.siapraja.model.Company;
import com.example.siapraja.model.Student;
import com.example.siapraja.model.Teacher;
import com.example.siapraja.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class MyUserDetails implements UserDetails {

    private final User user;
    private Object profile;

    public MyUserDetails(User user, Object profile) {
        this.user = user;
        this.profile = profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = switch (user.getRole()) {
            case 1 -> "ROLE_ADMIN";
            case 2 -> "ROLE_STUDENT";
            case 3 -> "ROLE_COMPANY";
            case 4 -> "ROLE_TEACHER";
            default -> "ROLE_USER";
        };
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public Long getUserId() {
        return user.getId();
    }

    public Long getStudentId() {
        if (profile instanceof Student) {
            return ((Student) profile).getId();
        }
        return null;
    }

    public Long getTeacherId() {
        if (profile instanceof Teacher) {
            return ((Teacher) profile).getId();
        }
        return null;
    }

    public Long getCompanyId() {
        if (profile instanceof Company) {
            return ((Company) profile).getId();
        }
        return null;
    }

    public Object getProfile() {
        return profile;
    }

    public <T> T getProfileAs(Class<T> clazz) {
        return clazz.isInstance(profile) ? clazz.cast(profile) : null;
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