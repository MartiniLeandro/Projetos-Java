package com.merx_commerce.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = " State cannot be blank.")
    private String state;

    @NotBlank(message = " City cannot be blank.")
    private String city;

    @NotBlank(message = " CEP cannot be blank.")
    private String cep;

    @NotBlank(message = " StreetName cannot be blank.")
    private String streetName;

    @NotBlank(message = " StreetNumber cannot be blank.")
    private String streetNumber;

    private String complement;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User cannot be null")
    private User user;

}
