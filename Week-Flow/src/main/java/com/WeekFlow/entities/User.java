package com.WeekFlow.entities;

import com.WeekFlow.entities.DTOS.UserRegisterDTO;
import com.WeekFlow.entities.ENUMS.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank(message = "Email cannot be null")
    private String email;

    @NotBlank(message = "Password cannot be null")
    private String password;

    private Roles role;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    public User(UserRegisterDTO data){
        this.id = data.id();
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
        this.role = Roles.USER;
        this.tasks = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == Roles.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_USER"),new SimpleGrantedAuthority("ROLE_ADMIN"));
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
