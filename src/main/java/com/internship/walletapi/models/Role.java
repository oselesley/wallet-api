package com.internship.walletapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.walletapi.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseModel{
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
    @JsonIgnore
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<SimpleGrantedAuthority> authorities = new HashSet<>();
}
