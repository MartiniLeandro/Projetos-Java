package com.BarberHub.demo.entities;

import com.BarberHub.demo.entities.ENUMS.StatusUsers;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Barbeiros")
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nome cannot be null")
    private String nome;

    @NotBlank(message = "telefone cannot be null")
    private String telefone;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "barbearia_id")
    private Barbearia barbearia;

    @Enumerated(EnumType.STRING)
    private StatusUsers status;


}
