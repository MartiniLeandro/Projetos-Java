package com.BarberHub.demo.entities;

import com.BarberHub.demo.entities.ENUMS.StatusUsers;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Barbearias")
public class Barbearia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nome cannot be null")
    private String nome;

    @NotBlank(message = "cnpj cannot be null")
    private String cnpj;

    @NotNull
    @OneToOne(mappedBy = "barbearia",cascade = CascadeType.ALL)
    private Endereco endereco;

    @NotBlank(message = "telefone cannot be null")
    private String telefone;

    @ElementCollection
    @CollectionTable(name = "barbearia_horarios", joinColumns = @JoinColumn(name = "barbearia_id"))
    private List<DataHoraBarbearia> horarios;

    @ElementCollection
    @CollectionTable(name = "barbearia_imagens", joinColumns = @JoinColumn(name = "barbearia_id"))
    private List<String> urlImagens;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL)
    private List<Barbeiro> barbeiros;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL)
    private List<Servico> servicos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private StatusUsers status;
}
