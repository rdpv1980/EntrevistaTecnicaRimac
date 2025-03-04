package com.tipo.cambio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tipo_cambio")
@Getter
@Setter
@NoArgsConstructor
public class TipoCambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String moneda;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal compra;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal venta;
}
