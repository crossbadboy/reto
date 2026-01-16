package cuenta_movimientos.com.adapter.in.controller;

import cuenta_movimientos.com.adapter.in.dto.request.MovimientoDTO;
import cuenta_movimientos.com.adapter.in.dto.response.MovimientoResponseDTO;
import cuenta_movimientos.com.domain.port.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/movimientos")
@Validated
public class MovimientoController {
    private final MovimientoService movimientoService;

    @Autowired
    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public Mono<MovimientoResponseDTO> createMovimiento(@Valid @RequestBody MovimientoDTO movimientoDTO) {
        return movimientoService.createMovimiento(movimientoDTO);
    }

    @PutMapping("/{id}")
    public Mono<MovimientoResponseDTO> updateMovimiento(@PathVariable String id, @Valid @RequestBody MovimientoDTO movimientoDTO) {
        return movimientoService.updateMovimiento(id, movimientoDTO);
    }

    @GetMapping
    public Flux<MovimientoResponseDTO> getAllMovimientos() {
        return movimientoService.getAllMovimientos();
    }

    @GetMapping("/{id}")
    public Mono<MovimientoResponseDTO> getMovimientoById(@PathVariable String id) {
        return movimientoService.getMovimientoById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMovimiento(@PathVariable String id) {
        return movimientoService.deleteMovimiento(id);
    }
}
