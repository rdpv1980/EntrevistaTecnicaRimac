package com.tipo.cambio.repository;

import com.tipo.cambio.model.TipoCambio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoCambioRepository extends JpaRepository<TipoCambio,Long> {

    Optional<TipoCambio> findByMoneda(String moneda);
}
