package com.internship.walletapi.models;

import com.internship.walletapi.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User extends BaseModel implements UserDetails {
    @Email(message = "Must be a valid email!")
    @Column(nullable = false, unique = true, name = "email")
    private String  email;

    @Column(nullable = false, unique = true, name = "user_name")
    private String username;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "firstname")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @ManyToOne
    private Role userRole;

    @Column(nullable = false, name = "main_currency")
    private String mainCurrency;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public User(Long id, String username) {
        this.setId(id);
        this.username = username;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole.getAuthorities();
    }
}
