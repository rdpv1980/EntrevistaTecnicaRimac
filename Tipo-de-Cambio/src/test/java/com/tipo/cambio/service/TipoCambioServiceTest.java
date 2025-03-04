package com.tipo.cambio.service;

import com.tipo.cambio.dto.TipoCambioRequest;
import com.tipo.cambio.dto.TipoCambioResponse;
import com.tipo.cambio.dto.TipoOperacion;
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

    private TipoCambioRequest request;
    private TipoCambio origen;
    private TipoCambio destino;

    @BeforeEach
    void setUp() {
        origen = new TipoCambio();
        origen.setMoneda("USD");
        origen.setCompra(new BigDecimal("3.80"));
        origen.setVenta(new BigDecimal("3.85"));

        destino = new TipoCambio();
        destino.setMoneda("PEN");
        destino.setCompra(new BigDecimal("1.00"));
        destino.setVenta(new BigDecimal("1.00"));

        request = new TipoCambioRequest(new BigDecimal("100.00"), "USD", "PEN");
    }

    @Test
    void aplicarTipoCambio_CalculoCorrecto() {
        //Given
        when(tipoCambioRepository.findByMoneda("USD")).thenReturn(Optional.of(origen));
        when(tipoCambioRepository.findByMoneda("PEN")).thenReturn(Optional.of(destino));

        //When
        TipoCambioResponse response = tipoCambioService.aplicarTipoCambio(request);

        //Then
        assertNotNull(response);
        assertEquals(new BigDecimal("100.00"), response.getMonto());
        assertEquals("USD", response.getMonedaOrigen());
        assertEquals("PEN", response.getMonedaDestino());
        assertEquals(TipoOperacion.VENTA, response.getTipoOperacion());
        verify(tipoCambioRepository, times(1)).findByMoneda("USD");
        verify(tipoCambioRepository, times(1)).findByMoneda("PEN");
    }

    @Test
    void aplicarTipoCambio_MonedaOrigenNoEncontrada() {
        when(tipoCambioRepository.findByMoneda("USD")).thenReturn(Optional.empty());

        assertThrows(TipoCambioNotFoundException.class, () -> tipoCambioService.aplicarTipoCambio(request));
        verify(tipoCambioRepository, times(1)).findByMoneda("USD");
        verify(tipoCambioRepository, never()).findByMoneda("PEN");
    }

    @Test
    void aplicarTipoCambio_MonedaDestinoNoEncontrada() {
        when(tipoCambioRepository.findByMoneda("USD")).thenReturn(Optional.of(origen));
        when(tipoCambioRepository.findByMoneda("PEN")).thenReturn(Optional.empty());

        assertThrows(TipoCambioNotFoundException.class, () -> tipoCambioService.aplicarTipoCambio(request));
        verify(tipoCambioRepository, times(1)).findByMoneda("USD");
        verify(tipoCambioRepository, times(1)).findByMoneda("PEN");
    }

}
