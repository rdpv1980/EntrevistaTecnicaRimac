package com.tipo.cambio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoCambioRequest {

    private BigDecimal monto;
    private String monedaOrigen;
    private String monedaDestino;
    TipoOperacion tipoOperacion;
}
