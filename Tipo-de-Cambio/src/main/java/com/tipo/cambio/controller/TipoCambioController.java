package com.tipo.cambio.controller;

import com.tipo.cambio.dto.TipoCambioRequest;
import com.tipo.cambio.dto.TipoCambioResponse;
import com.tipo.cambio.dto.TipoOperacion;
import com.tipo.cambio.model.TipoCambio;
import com.tipo.cambio.service.TipoCambioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/api/tipo-cambio")
public class TipoCambioController {

    private TipoCambioService tipoCambioService;

    public TipoCambioController(TipoCambioService tipoCambioService) {
        this.tipoCambioService = tipoCambioService;
    }

    @GetMapping("/obtener")
    public ResponseEntity<TipoCambioResponse> obtenerTipoCambio(
            @RequestParam BigDecimal monto,
            @RequestParam String monedaOrigen,
            @RequestParam String monedaDestino,
            @RequestParam TipoOperacion tipoOperacion){

        TipoCambioRequest request= new TipoCambioRequest(monto,monedaOrigen,monedaDestino,tipoOperacion);
        TipoCambioResponse response= tipoCambioService.aplicarTipoCambio(request);

        return ResponseEntity.ok(response);

    }

    @PutMapping("/actualizar")
    public ResponseEntity<TipoCambio> actualizarTipoCambio(@RequestParam Long id, @RequestBody TipoCambio tipoCambio) {
        TipoCambio actualizado = tipoCambioService.actualizarTipoCambio(id,tipoCambio);
        return ResponseEntity.ok(actualizado);
    }
}
