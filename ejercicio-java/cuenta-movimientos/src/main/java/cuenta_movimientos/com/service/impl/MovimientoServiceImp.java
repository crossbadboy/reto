package cuenta_movimientos.com.service.impl;

import cuenta_movimientos.com.dto.request.MovimientoDTO;
import cuenta_movimientos.com.dto.response.MovimientoResponseDTO;
import cuenta_movimientos.com.exception.NegocioException;
import cuenta_movimientos.com.model.Cuenta;
import cuenta_movimientos.com.model.Movimiento;
import cuenta_movimientos.com.repository.interfaces.CuentaRepository;
import cuenta_movimientos.com.repository.interfaces.MovimientoRepository;
import cuenta_movimientos.com.service.interfaces.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientoServiceImp implements MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public static final String DEPOSITO = "DEPOSITO";
    public static final String RETIRO = "RETIRO";

    @Override
    public MovimientoResponseDTO createMovimiento(MovimientoDTO movimientoDTO) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimientoDTO.getNumeroCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta con numero " + movimientoDTO.getNumeroCuenta() + " no encontrada."));
        Double saldoActual = calcularSaldoActual(cuenta);
        String tipoMovimiento = movimientoDTO.getTipoMovimiento().toUpperCase();
        Double valor = movimientoDTO.getValor();
        if (RETIRO.equals(tipoMovimiento) && saldoActual < valor) {
            throw new NegocioException("Saldo no disponible.");
        }
        Double nuevoSaldo = switch (tipoMovimiento) {
            case DEPOSITO -> saldoActual + valor;
            case RETIRO -> saldoActual - valor;
            default -> throw new NegocioException("Tipo de movimiento no válido.");
        };
        Movimiento movimiento = Movimiento.builder().fecha(new Date()).tipoMovimiento(tipoMovimiento)
                .valor(valor).saldo(nuevoSaldo).cuenta(cuenta).build();
        return mapToResponseDTO(movimientoRepository.save(movimiento));
    }

    private Double calcularSaldoActual(Cuenta cuenta) {
        List<Movimiento> movimientos = movimientoRepository.findByCuentaId(cuenta.getId());
        Double saldo = movimientos.stream()
                .mapToDouble(movimiento -> switch (movimiento.getTipoMovimiento().toUpperCase()) {
                    case DEPOSITO -> movimiento.getValor();
                    case RETIRO -> -movimiento.getValor();
                    default -> 0.0;
                })
                .sum();
        return cuenta.getSaldoInicial() + saldo;
    }

    @Override
    public MovimientoResponseDTO updateMovimiento(Long id, MovimientoDTO movimientoDTO) {
        Movimiento movimientoExistente = movimientoRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Movimiento con ID " + id + " no encontrado."));
        Cuenta cuenta = cuentaRepository.findById(movimientoDTO.getCuentaId())
                .orElseThrow(() -> new NegocioException("Cuenta con ID " + movimientoDTO.getCuentaId() + " no encontrada."));
        Double saldoActual = calcularSaldoActual(cuenta);
        String tipoMovimiento = movimientoDTO.getTipoMovimiento().toUpperCase();
        if (RETIRO.equals(tipoMovimiento) && saldoActual < movimientoDTO.getValor()) {
            throw new NegocioException("Saldo no disponible.");
        }
        movimientoExistente.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimientoExistente.setValor(movimientoDTO.getValor());
        movimientoExistente.setCuenta(cuenta);
        return mapToResponseDTO(movimientoRepository.save(movimientoExistente));
    }

    @Override
    public List<MovimientoResponseDTO> getAllMovimientos() {
        return movimientoRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public MovimientoResponseDTO getMovimientoById(Long id) {
        return mapToResponseDTO(
                movimientoRepository.findById(id)
                        .orElseThrow(() -> new NegocioException("Movimiento con ID " + id + " no encontrado.")));
    }

    @Override
    public void deleteMovimiento(Long id) {
        if (!movimientoRepository.existsById(id)) {
            throw new NegocioException("Movimiento con ID " + id + " no encontrado para eliminar.");
        }
        movimientoRepository.deleteById(id);
    }

    private MovimientoResponseDTO mapToResponseDTO(Movimiento movimiento) {
        return MovimientoResponseDTO.builder()
                .id(movimiento.getMovimientoId()).fecha(convertToLocalDate(movimiento.getFecha()))
                .tipoMovimiento(movimiento.getTipoMovimiento()).valor(movimiento.getValor())
                .saldo(movimiento.getSaldo()).cuentaId(movimiento.getCuenta().getId()).numeroCuenta(movimiento.getCuenta().getNumeroCuenta()).build();
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
