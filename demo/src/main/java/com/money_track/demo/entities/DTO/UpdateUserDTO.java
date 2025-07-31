package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.User;

public class UpdateUserDTO {
    private String name;
    private String email;
    private String password;

    public UpdateUserDTO(User user){
        this.name = user.getName();
        this.email = user.getName();
        this.password = user.getPassword();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
