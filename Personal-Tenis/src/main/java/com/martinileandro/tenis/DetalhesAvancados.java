package com.martinileandro.tenis;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "detalhes_avancados")
public class DetalhesAvancados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
