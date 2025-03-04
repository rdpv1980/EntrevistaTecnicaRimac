package com.tipo.cambio.repository;

import com.tipo.cambio.model.TipoCambio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TipoCambioRepositoryTest {

    @Mock
    private TipoCambioRepository tipoCambioRepository;

    @Test
    void testFindByMoneda() {

        TipoCambio tipoCambio = new TipoCambio();
        tipoCambio.setMoneda("USD");
        tipoCambio.setCompra(new BigDecimal("3.80"));
        tipoCambio.setVenta(new BigDecimal("3.85"));

        //Given
        when(tipoCambioRepository.findByMoneda("USD")).thenReturn(Optional.of(tipoCambio));

        //When
        Optional<TipoCambio> resultado = tipoCambioRepository.findByMoneda("USD");

        //Then
        assertTrue(resultado.isPresent());
        assertEquals("USD", resultado.get().getMoneda());
    }
}
