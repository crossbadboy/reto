package cuenta_movimientos.com.adapter.in.controller;

import cuenta_movimientos.com.adapter.in.dto.request.CuentaDTO;
import cuenta_movimientos.com.adapter.in.dto.response.CuentaResponseDTO;
import cuenta_movimientos.com.domain.port.CuentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cuentas")
@Validated
public class CuentaController {
    private final CuentaService cuentaService;

    @Autowired
    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public Flux<CuentaResponseDTO> getAllCuentas() {
        return cuentaService.getAllCuentas();
    }

    @GetMapping("/{id}")
    public Mono<CuentaResponseDTO> getCuentaById(@PathVariable String id) {
        return cuentaService.getCuentaById(id);
    }

    @PostMapping
    public Mono<CuentaResponseDTO> createCuenta(@Valid @RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.saveCuenta(cuentaDTO);
    }

    @PutMapping("/{id}")
    public Mono<CuentaResponseDTO> updateCuenta(@PathVariable String id, @Valid @RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.updateCuenta(id, cuentaDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCuenta(@PathVariable String id) {
        return cuentaService.deleteCuenta(id);
    }
}
