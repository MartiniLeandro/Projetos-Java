package com.merx_commerce.demo.entities;

import com.merx_commerce.demo.entities.Enums.StatusOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "date cannot be null")
    private LocalDateTime date;

    @NotNull(message = "status cannot be null")
    private StatusOrder status;

    @NotNull(message = "total cannot be null")
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "user cannot be null")
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    @NotNull(message = "itens cannot be null")
    private List<OrderItems> items;
}
