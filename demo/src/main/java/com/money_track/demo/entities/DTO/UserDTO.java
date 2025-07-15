package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;

public class UserDTO {
    private final Long id;
    private String name;
    private String cpf;
    private String email;
    private Roles role;

    public UserDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.cpf = user.getCpf();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
