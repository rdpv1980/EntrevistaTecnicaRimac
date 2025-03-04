package com.tipo.cambio.service;

import com.tipo.cambio.dto.TipoCambioRequest;
import com.tipo.cambio.dto.TipoCambioResponse;
import com.tipo.cambio.exception.TipoCambioNotFoundException;
import com.tipo.cambio.model.TipoCambio;
import com.tipo.cambio.repository.TipoCambioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoCambioServiceTest {

    @Mock
    private TipoCambioRepository tipoCambioRepository;

    @InjectMocks
    private TipoCambioServiceImpl tipoCambioService;

    private TipoCambio tipoCambioUsd;
    private TipoCambio tipoCambioEur;

    @BeforeEach
    void setUp() {
        tipoCambioUsd = new TipoCambio();
        tipoCambioUsd.setMoneda("USD");
        tipoCambioUsd.setCompra(new BigDecimal("3.80"));
        tipoCambioUsd.setVenta(new BigDecimal("3.85"));

        tipoCambioEur = new TipoCambio();
        tipoCambioEur.setMoneda("EUR");
        tipoCambioEur.setCompra(new BigDecimal("4.20"));
        tipoCambioEur.setVenta(new BigDecimal("4.25"));
    }

    @Test
    void testAplicarTipoCambioCompra() {
        // Simular que los datos existen en el repository
        when(tipoCambioRepository.findByMoneda("USD")).thenReturn(Optional.of(tipoCambioUsd));
        when(tipoCambioRepository.findByMoneda("EUR")).thenReturn(Optional.of(tipoCambioEur));

        // Crear una solicitud de cambio de 100 USD a EUR en modo COMPRA
        TipoCambioRequest request = new TipoCambioRequest(new BigDecimal("100"), "USD", "EUR");
        TipoCambioResponse response = tipoCambioService.aplicarTipoCambio(request);

        // Verificaciones
        assertNotNull(response);
        assertEquals(new BigDecimal("100"), response.getMonto());
        assertEquals("USD", response.getMonedaOrigen());
        assertEquals("EUR", response.getMonedaDestino());
        assertEquals(new BigDecimal("23.81"), response.getMontoConTipoCambio());
        assertEquals(new BigDecimal("4.20"), response.getValorTipoCambio());

        verify(tipoCambioRepository, times(1)).findByMoneda("USD");
        verify(tipoCambioRepository, times(1)).findByMoneda("EUR");
    }

    @Test
    void testAplicarTipoCambioVenta() {
        when(tipoCambioRepository.findByMoneda("USD")).thenReturn(Optional.of(tipoCambioUsd));
        when(tipoCambioRepository.findByMoneda("EUR")).thenReturn(Optional.of(tipoCambioEur));

        // Cambiar 100 EUR a USD en modo VENTA
        TipoCambioRequest request = new TipoCambioRequest(new BigDecimal("100"), "EUR", "USD");
        TipoCambioResponse response = tipoCambioService.aplicarTipoCambio(request);

        // Verificaciones
        assertNotNull(response);
        assertEquals(new BigDecimal("100"), response.getMonto());
        assertEquals("EUR", response.getMonedaOrigen());
        assertEquals("USD", response.getMonedaDestino());
        assertEquals(new BigDecimal("4.25"), response.getValorTipoCambio());

        verify(tipoCambioRepository, times(1)).findByMoneda("EUR");
        verify(tipoCambioRepository, times(1)).findByMoneda("USD");
    }

    @Test
    void testTipoCambioNoEncontrado() {
        when(tipoCambioRepository.findByMoneda("USD")).thenReturn(Optional.empty());

        TipoCambioRequest request = new TipoCambioRequest(new BigDecimal("100"), "USD", "EUR");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            tipoCambioService.aplicarTipoCambio(request);
        });

        assertEquals("Tipo de cambio no encontrado", exception.getMessage());

        verify(tipoCambioRepository, times(1)).findByMoneda("USD");
        //verify(tipoCambioRepository, never()).findByMoneda("EUR");
    }

    @Test
    void testActualizarTipoCambio() {
        when(tipoCambioRepository.findById(1L)).thenReturn(Optional.of(tipoCambioUsd));
        when(tipoCambioRepository.save(any(TipoCambio.class))).thenReturn(tipoCambioUsd);

        TipoCambio tipoCambioActualizado = new TipoCambio();
        tipoCambioActualizado.setCompra(new BigDecimal("3.90"));
        tipoCambioActualizado.setVenta(new BigDecimal("3.95"));

        TipoCambio resultado = tipoCambioService.actualizarTipoCambio(1L, tipoCambioActualizado);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("3.90"), resultado.getCompra());
        assertEquals(new BigDecimal("3.95"), resultado.getVenta());

        verify(tipoCambioRepository, times(1)).findById(1L);
        verify(tipoCambioRepository, times(1)).save(any(TipoCambio.class));
    }

    @Test
    void testActualizarTipoCambioNoExistente() {
        when(tipoCambioRepository.findById(99L)).thenReturn(Optional.empty());

        TipoCambio tipoCambioActualizado = new TipoCambio();
        tipoCambioActualizado.setCompra(new BigDecimal("3.90"));
        tipoCambioActualizado.setVenta(new BigDecimal("3.95"));

        assertThrows(TipoCambioNotFoundException.class, () -> {
            tipoCambioService.actualizarTipoCambio(99L, tipoCambioActualizado);
        });

        verify(tipoCambioRepository, times(1)).findById(99L);
        verify(tipoCambioRepository, never()).save(any(TipoCambio.class));
    }
}
