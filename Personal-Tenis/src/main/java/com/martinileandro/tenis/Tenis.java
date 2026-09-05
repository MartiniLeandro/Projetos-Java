package com.martinileandro.tenis;

import com.martinileandro.marca.Marca;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tenis")
public class Tenis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O modelo não pode ser nulo")
    private String modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @ElementCollection
    @CollectionTable(name = "tenis_imagens", joinColumns = @JoinColumn(name = "tenis_id"))
    @Column(name = "url_imagem")
    private List<String> urlImagens;

    @Enumerated(EnumType.STRING)
    private TenisGenero genero;

    @Enumerated(EnumType.STRING)
    private TenisStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_tecnica_id")
    private FichaTecnica fichaTecnica;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detalhes_avancados_id")
    private DetalhesAvancados detalhesAvancados;


}
