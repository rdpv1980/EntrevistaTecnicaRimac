package com.tipo.cambio.exception;

public class TipoCambioNotFoundException extends RuntimeException {
    public TipoCambioNotFoundException(Long id) {
        super("Tipo de cambio no encontrado con ID: " + id);
    }
}