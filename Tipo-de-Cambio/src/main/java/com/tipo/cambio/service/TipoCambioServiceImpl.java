package com.tipo.cambio.service;

import com.tipo.cambio.dto.TipoCambioRequest;
import com.tipo.cambio.dto.TipoCambioResponse;
import com.tipo.cambio.dto.TipoOperacion;
import com.tipo.cambio.exception.TipoCambioNotFoundException;
import com.tipo.cambio.model.TipoCambio;
import com.tipo.cambio.repository.TipoCambioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class TipoCambioServiceImpl implements TipoCambioService {

    private TipoCambioRepository tipoCambioRepository;

    public TipoCambioServiceImpl(TipoCambioRepository tipoCambioRepository) {
        this.tipoCambioRepository = tipoCambioRepository;
    }

    @Override
    public TipoCambioResponse aplicarTipoCambio(TipoCambioRequest request) {

        validarRequest(request);

        TipoCambio origen = tipoCambioRepository.findByMoneda(request.getMonedaOrigen())
                .orElseThrow(() -> new TipoCambioNotFoundException("Moneda origen no encontrada: " + request.getMonedaOrigen()));

        TipoCambio destino = tipoCambioRepository.findByMoneda(request.getMonedaDestino())
                .orElseThrow(() -> new TipoCambioNotFoundException("Moneda destino no encontrada: " + request.getMonedaDestino()));

        return calcularTipoCambio(request, origen, destino);
    }

    @Override
    public TipoCambio actualizarTipoCambio(Long id, TipoCambio tipoCambio) {
        return tipoCambioRepository.findById(id)
                .map(tipoCambioExistente -> {
                    tipoCambioExistente.setCompra(tipoCambio.getCompra());
                    tipoCambioExistente.setVenta(tipoCambio.getVenta());
                    return tipoCambioRepository.save(tipoCambioExistente);
                })
                .orElseThrow(() -> new TipoCambioNotFoundException(id));
    }

    private void validarRequest(TipoCambioRequest request) {
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (request.getMonedaOrigen() == null || request.getMonedaOrigen().isBlank()) {
            throw new IllegalArgumentException("La moneda de origen es obligatoria");
        }
        if (request.getMonedaDestino() == null || request.getMonedaDestino().isBlank()) {
            throw new IllegalArgumentException("La moneda de destino es obligatoria");
        }
    }

    private TipoCambioResponse calcularTipoCambio(TipoCambioRequest request, TipoCambio origen, TipoCambio destino) {
        boolean esCompra = origen.getVenta().compareTo(destino.getVenta()) < 0;
        TipoOperacion tipoOperacion = esCompra ? TipoOperacion.COMPRA : TipoOperacion.VENTA;
        BigDecimal tipoCambioAplicado = esCompra ? destino.getCompra() : origen.getVenta();
        BigDecimal montoConTipoCambio = esCompra
                ? request.getMonto().divide(tipoCambioAplicado, 2, RoundingMode.HALF_UP)
                : request.getMonto().multiply(tipoCambioAplicado);

        return new TipoCambioResponse(
                request.getMonto(),
                request.getMonedaOrigen(),
                request.getMonedaDestino(),
                montoConTipoCambio,
                tipoOperacion,
                tipoCambioAplicado
        );
    }
}