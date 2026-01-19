package cuenta_movimientos.com.adapter.in.controller;

import cuenta_movimientos.com.adapter.in.dto.request.CuentaDTO;
import cuenta_movimientos.com.adapter.in.dto.response.CuentaResponseDTO;
import cuenta_movimientos.com.domain.port.CuentaPort;
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
    private final CuentaPort cuentaPort;

    @Autowired
    public CuentaController(CuentaPort cuentaPort) {
        this.cuentaPort = cuentaPort;
    }

    @GetMapping
    public Flux<CuentaResponseDTO> getAllCuentas() {
        return cuentaPort.getAllCuentas();
    }

    @GetMapping("/{id}")
    public Mono<CuentaResponseDTO> getCuentaById(@PathVariable String id) {
        return cuentaPort.getCuentaById(id);
    }

    @PostMapping
    public Mono<CuentaResponseDTO> createCuenta(@Valid @RequestBody CuentaDTO cuentaDTO) {
        return cuentaPort.saveCuenta(cuentaDTO);
    }

    @PutMapping("/{id}")
    public Mono<CuentaResponseDTO> updateCuenta(@PathVariable String id, @Valid @RequestBody CuentaDTO cuentaDTO) {
        return cuentaPort.updateCuenta(id, cuentaDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCuenta(@PathVariable String id) {
        return cuentaPort.deleteCuenta(id);
    }
}
