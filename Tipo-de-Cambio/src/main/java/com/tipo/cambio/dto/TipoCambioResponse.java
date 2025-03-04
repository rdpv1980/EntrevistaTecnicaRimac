package com.tipo.cambio.dto;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoCambioResponse {
    private BigDecimal monto;
    private String monedaOrigen;
    private String monedaDestino;
    private BigDecimal  montoConTipoCambio;
    private TipoOperacion  tipoOperacion;
    private BigDecimal valorTipoCambio;
}
