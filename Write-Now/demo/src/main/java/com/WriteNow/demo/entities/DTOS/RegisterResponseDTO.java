package com.WriteNow.demo.entities.DTOS;

import com.WriteNow.demo.entities.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","name","email","address"})
public class RegisterResponseDTO {
    private Long id;
    private String name;
    private String email;
    private CepApiResponse address;

    public RegisterResponseDTO(User user, CepApiResponse address){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CepApiResponse getAddress() {
        return address;
    }

    public void setAddress(CepApiResponse address) {
        this.address = address;
    }
}
