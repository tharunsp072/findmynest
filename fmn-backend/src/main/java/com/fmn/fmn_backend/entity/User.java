package com.fmn.fmn_backend.entity;


import java.util.Collection;
import java.util.HashSet;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fmn.fmn_backend.model.Role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @Column(name = "role",nullable = false)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> role = new HashSet<>();

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private OwnerProfile ownerProfile;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private TenantProfile tenantProfile;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return role.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
        .collect(Collectors.toList());
    }

}
