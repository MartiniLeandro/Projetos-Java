package com.BarberHub.demo.entities;

import com.BarberHub.demo.entities.ENUMS.RoleUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The email cannot be null")
    private String email;

    @NotBlank(message = "The password cannot be null")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleUser role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cliente cliente;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Barbearia barbearia;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Barbeiro barbeiro;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == RoleUser.ADMIN)  return List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_BARBEARIA"),
                new SimpleGrantedAuthority("ROLE_BARBEIRO"),
                new SimpleGrantedAuthority("ROLE_CLIENTE"));
        if(this.role == RoleUser.CLIENTE) return  List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));
        if(this.role == RoleUser.BARBEARIA) return  List.of(new SimpleGrantedAuthority("ROLE_BARBEARIA"));
        return List.of(new SimpleGrantedAuthority("ROLE_BARBEIRO"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
