package cuenta_movimientos.com.controller;

import cuenta_movimientos.com.dto.request.MovimientoDTO;
import cuenta_movimientos.com.dto.response.MovimientoResponseDTO;
import cuenta_movimientos.com.service.interfaces.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<MovimientoResponseDTO> createMovimiento(@Valid @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoResponseDTO nuevoMovimiento = movimientoService.createMovimiento(movimientoDTO);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> updateMovimiento(@PathVariable Long id, @Valid @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoResponseDTO movimientoActualizado = movimientoService.updateMovimiento(id, movimientoDTO);
        return new ResponseEntity<>(movimientoActualizado, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> getAllMovimientos() {
        List<MovimientoResponseDTO> movimientos = movimientoService.getAllMovimientos();
        return new ResponseEntity<>(movimientos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> getMovimientoById(@PathVariable Long id) {
        MovimientoResponseDTO movimiento = movimientoService.getMovimientoById(id);
        return new ResponseEntity<>(movimiento, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable Long id) {
        movimientoService.deleteMovimiento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
