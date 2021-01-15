package com.internship.walletapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.walletapi.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseModel{
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
}
