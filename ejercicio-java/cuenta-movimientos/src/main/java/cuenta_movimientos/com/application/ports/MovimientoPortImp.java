package cuenta_movimientos.com.application.ports;

import cuenta_movimientos.com.adapter.in.dto.request.MovimientoDTO;
import cuenta_movimientos.com.adapter.in.dto.response.MovimientoResponseDTO;
import cuenta_movimientos.com.domain.exception.NegocioException;
import cuenta_movimientos.com.domain.model.Cuenta;
import cuenta_movimientos.com.domain.model.Movimiento;
import cuenta_movimientos.com.domain.repository.CuentaRepository;
import cuenta_movimientos.com.domain.repository.MovimientoRepository;
import cuenta_movimientos.com.domain.port.MovimientoPort;
import cuenta_movimientos.com.mapper.MovimientoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientoPortImp implements MovimientoPort {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;

    @Autowired
    public MovimientoPortImp(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository, MovimientoMapper movimientoMapper) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.movimientoMapper = movimientoMapper;
    }

    public static final String DEPOSITO = "DEPOSITO";
    public static final String RETIRO = "RETIRO";

    @Override
    @Transactional
    public Mono<MovimientoResponseDTO> createMovimiento(MovimientoDTO movimientoDTO) {
        return Mono.fromCallable(() -> {
            Movimiento movimiento = calcularSaldoMovimiento(movimientoDTO);
            return movimientoMapper.toDto(movimientoRepository.save(movimiento));
        });
    }

    private Movimiento calcularSaldoMovimiento(MovimientoDTO movimientoDTO) {
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
        return Movimiento.builder().fecha(new Date()).tipoMovimiento(tipoMovimiento)
                .valor(valor).saldo(nuevoSaldo).cuenta(cuenta).build();
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
    @Transactional
    public Mono<MovimientoResponseDTO> updateMovimiento(String id, MovimientoDTO movimientoDTO) {
        return Mono.fromCallable(() -> movimientoRepository.findById(Long.valueOf(id)))
                .flatMap(optionalMovimiento -> {
                    if (optionalMovimiento.isEmpty()) {
                        return Mono.error(new NegocioException("Movimiento con ID " + id + " no encontrado."));
                    }
                    Movimiento updated = obtenerMovimientoActualizado(optionalMovimiento, movimientoDTO);
                    return Mono.just(movimientoMapper.toDto(updated));
                });
    }

    private Movimiento obtenerMovimientoActualizado(Optional<Movimiento> optionalMovimiento, MovimientoDTO movimientoDTO) {
        Movimiento movimientoExistente = optionalMovimiento.get();
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
        return movimientoRepository.save(movimientoExistente);
    }

    @Override
    public Flux<MovimientoResponseDTO> getAllMovimientos() {
        return Flux.defer(() -> Flux.fromIterable(movimientoRepository.findAll())
                .map(movimientoMapper::toDto));
    }

    @Override
    public Mono<MovimientoResponseDTO> getMovimientoById(String id) {
        return Mono.fromCallable(() -> movimientoRepository.findById(Long.valueOf(id)))
                .flatMap(optionalMovimiento -> optionalMovimiento
                        .map(movimiento -> Mono.just(movimientoMapper.toDto(movimiento)))
                        .orElseGet(() -> Mono.error(new NegocioException("Movimiento con ID " + id + " no encontrado."))));
    }

    @Override
    @Transactional
    public Mono<Void> deleteMovimiento(String id) {
        return Mono.fromCallable(() -> movimientoRepository.findById(Long.valueOf(id)))
                .flatMap(optionalMovimiento -> {
                    if (optionalMovimiento.isPresent()) {
                        movimientoRepository.delete(optionalMovimiento.get());
                        return Mono.empty();
                    } else {
                        return Mono.error(new NegocioException("Movimiento con ID " + id + " no encontrado para eliminar."));
                    }
                });
    }
}
