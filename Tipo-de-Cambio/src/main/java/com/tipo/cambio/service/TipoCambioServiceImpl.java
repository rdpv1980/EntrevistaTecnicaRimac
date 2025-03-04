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
        Optional<TipoCambio> origen = tipoCambioRepository.findByMoneda(request.getMonedaOrigen());
        Optional<TipoCambio> destino = tipoCambioRepository.findByMoneda(request.getMonedaDestino());

        if (origen.isEmpty() || destino.isEmpty()) {
            throw new RuntimeException("Tipo de cambio no encontrado");
        }

        BigDecimal montoConTipoCambio;
        BigDecimal tipoCambioAplicado;

        if (request.getTipoOperacion() == TipoOperacion.COMPRA) {
            // Si estamos COMPRANDO moneda destino, usamos su VENTA
            tipoCambioAplicado = destino.get().getVenta();
            montoConTipoCambio = request.getMonto().divide(tipoCambioAplicado, 4, RoundingMode.HALF_UP);
        } else {
            // Si estamos VENDIENDO moneda origen, usamos su COMPRA
            tipoCambioAplicado = origen.get().getCompra();
            montoConTipoCambio = request.getMonto().multiply(tipoCambioAplicado);
        }

        TipoCambioResponse response = new TipoCambioResponse();
        response.setMonto(request.getMonto());
        response.setMonedaOrigen(request.getMonedaOrigen());
        response.setMonedaDestino(request.getMonedaDestino());
        response.setMontoConTipoCambio(montoConTipoCambio);
        response.setTipoOperacion(request.getTipoOperacion());
        response.setValorTipoCambio(tipoCambioAplicado);

        return response;
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
}