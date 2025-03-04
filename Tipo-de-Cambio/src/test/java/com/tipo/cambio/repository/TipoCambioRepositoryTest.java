package com.tipo.cambio.repository;

import com.tipo.cambio.model.TipoCambio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // Carga solo la capa de persistencia con H2
class TipoCambioRepositoryTest {

    @Autowired
    private TipoCambioRepository tipoCambioRepository;

    @Test
    void testGuardarYBuscarPorMoneda() {
        // Arrange
        TipoCambio tipoCambio = new TipoCambio();
        tipoCambio.setMoneda("USD");
        tipoCambio.setCompra(new BigDecimal("3.80"));
        tipoCambio.setVenta(new BigDecimal("3.85"));

        tipoCambioRepository.save(tipoCambio);

        // Act
        Optional<TipoCambio> encontrado = tipoCambioRepository.findByMoneda("USD");

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCompra()).isEqualByComparingTo("3.80");
        assertThat(encontrado.get().getVenta()).isEqualByComparingTo("3.85");
    }

    @Test
    void testBuscarMonedaInexistente() {
        Optional<TipoCambio> encontrado = tipoCambioRepository.findByMoneda("EUR");
        assertThat(encontrado).isNotPresent();
    }
}
