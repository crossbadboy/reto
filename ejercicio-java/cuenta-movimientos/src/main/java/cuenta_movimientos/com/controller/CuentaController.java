package cuenta_movimientos.com.controller;

import cuenta_movimientos.com.dto.request.CuentaDTO;
import cuenta_movimientos.com.dto.response.CuentaResponseDTO;
import cuenta_movimientos.com.service.interfaces.CuentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@Validated
public class CuentaController {

    private final CuentaService cuentaService;

    @Autowired
    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<CuentaResponseDTO> createCuenta(@Valid @RequestBody CuentaDTO cuentaDTO) {
        CuentaResponseDTO nuevaCuenta = cuentaService.saveCuenta(cuentaDTO);
        return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> updateCuenta(@PathVariable Long id, @Valid @RequestBody CuentaDTO cuentaDTO) {
        CuentaResponseDTO cuentaActualizada = cuentaService.updateCuenta(id, cuentaDTO);
        return new ResponseEntity<>(cuentaActualizada, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> getAllCuentas() {
        List<CuentaResponseDTO> cuentas = cuentaService.getAllCuentas();
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> getCuentaById(@PathVariable Long id) {
        CuentaResponseDTO cuenta = cuentaService.getCuentaById(id);
        return new ResponseEntity<>(cuenta, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        cuentaService.deleteCuenta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
