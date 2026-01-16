package cuenta_movimientos.com.application.ports;

import cuenta_movimientos.com.adapter.in.dto.request.MovimientoDTO;
import cuenta_movimientos.com.adapter.in.dto.response.MovimientoResponseDTO;
import cuenta_movimientos.com.domain.exception.NegocioException;
import cuenta_movimientos.com.domain.model.Cuenta;
import cuenta_movimientos.com.domain.model.Movimiento;
import cuenta_movimientos.com.domain.repository.CuentaRepository;
import cuenta_movimientos.com.domain.repository.MovimientoRepository;
import cuenta_movimientos.com.domain.port.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class MovimientoServiceImp implements MovimientoService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    @Autowired
    public MovimientoServiceImp(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public static final String DEPOSITO = "DEPOSITO";
    public static final String RETIRO = "RETIRO";

    @Override
    @Transactional
    public Mono<MovimientoResponseDTO> createMovimiento(MovimientoDTO movimientoDTO) {
        return Mono.fromCallable(() -> {
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
        });
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
                    Movimiento movimientoExistente = optionalMovimiento.get();
                    Cuenta cuenta = cuentaRepository.findById(movimientoDTO.getCuentaId())
                            .orElseThrow(() -> new NegocioException("Cuenta con ID " + movimientoDTO.getCuentaId() + " no encontrada."));
                    Double saldoActual = calcularSaldoActual(cuenta);
                    String tipoMovimiento = movimientoDTO.getTipoMovimiento().toUpperCase();
                    if (RETIRO.equals(tipoMovimiento) && saldoActual < movimientoDTO.getValor()) {
                        return Mono.error(new NegocioException("Saldo no disponible."));
                    }
                    movimientoExistente.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
                    movimientoExistente.setValor(movimientoDTO.getValor());
                    movimientoExistente.setCuenta(cuenta);
                    Movimiento updated = movimientoRepository.save(movimientoExistente);
                    return Mono.just(mapToResponseDTO(updated));
                });
    }

    @Override
    public Flux<MovimientoResponseDTO> getAllMovimientos() {
        return Flux.defer(() -> Flux.fromIterable(movimientoRepository.findAll())
                .map(this::mapToResponseDTO));
    }

    @Override
    public Mono<MovimientoResponseDTO> getMovimientoById(String id) {
        return Mono.fromCallable(() -> movimientoRepository.findById(Long.valueOf(id)))
                .flatMap(optionalMovimiento -> optionalMovimiento
                        .map(movimiento -> Mono.just(mapToResponseDTO(movimiento)))
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

    private MovimientoResponseDTO mapToResponseDTO(Movimiento movimiento) {
        return MovimientoResponseDTO.builder()
                .id(movimiento.getMovimientoId())
                .fecha(movimiento.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .valor(movimiento.getValor())
                .saldo(movimiento.getSaldo())
                .cuentaId(movimiento.getCuenta().getId())
                .numeroCuenta(movimiento.getCuenta().getNumeroCuenta())
                .build();
    }
}
