package com.tipo.cambio.service;

import com.tipo.cambio.dto.TipoCambioRequest;
import com.tipo.cambio.dto.TipoCambioResponse;
import com.tipo.cambio.model.TipoCambio;

public interface TipoCambioService {
    TipoCambioResponse aplicarTipoCambio(TipoCambioRequest request);
    TipoCambio actualizarTipoCambio(Long id,TipoCambio tipoCambio);
}

