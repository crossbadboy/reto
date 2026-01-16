package cuenta_movimientos.com.application.ports;

import cuenta_movimientos.com.adapter.in.dto.request.CuentaDTO;
import cuenta_movimientos.com.adapter.in.dto.response.CuentaResponseDTO;
import cuenta_movimientos.com.domain.exception.NegocioException;
import cuenta_movimientos.com.domain.model.Cuenta;
import cuenta_movimientos.com.domain.repository.CuentaRepository;
import cuenta_movimientos.com.domain.port.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CuentaServiceImp implements CuentaService {
    private final CuentaRepository cuentaRepository;

    @Autowired
    public CuentaServiceImp(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    @Transactional
    public Mono<CuentaResponseDTO> saveCuenta(CuentaDTO cuentaDTO) {
        return Mono.fromCallable(() -> {
            Cuenta cuenta = mapToEntity(cuentaDTO);
            Cuenta saved = cuentaRepository.save(cuenta);
            return mapToResponseDTO(saved);
        });
    }

    @Override
    @Transactional
    public Mono<CuentaResponseDTO> updateCuenta(String id, CuentaDTO cuentaDTO) {
        return Mono.fromCallable(() -> cuentaRepository.findById(Long.valueOf(id)))
                .flatMap(optionalCuenta -> {
                    if (optionalCuenta.isEmpty()) {
                        return Mono.error(new NegocioException("Cuenta con ID " + id + " no encontrada para actualizar."));
                    }
                    Cuenta cuenta = optionalCuenta.get();
                    cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
                    cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
                    cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());
                    cuenta.setEstado(cuentaDTO.getEstado());
                    Cuenta updated = cuentaRepository.save(cuenta);
                    return Mono.just(mapToResponseDTO(updated));
                });
    }

    @Override
    public Flux<CuentaResponseDTO> getAllCuentas() {
        return Flux.defer(() -> Flux.fromIterable(cuentaRepository.findAll())
                .map(this::mapToResponseDTO));
    }

    @Override
    public Mono<CuentaResponseDTO> getCuentaById(String id) {
        return Mono.fromCallable(() -> cuentaRepository.findById(Long.valueOf(id)))
                .flatMap(optionalCuenta -> optionalCuenta
                        .map(cuenta -> Mono.just(mapToResponseDTO(cuenta)))
                        .orElseGet(() -> Mono.error(new NegocioException("Cuenta con ID " + id + " no encontrada."))));
    }

    @Override
    @Transactional
    public Mono<Void> deleteCuenta(String id) {
        return Mono.fromCallable(() -> cuentaRepository.findById(Long.valueOf(id)))
                .flatMap(optionalCuenta -> {
                    if (optionalCuenta.isPresent()) {
                        cuentaRepository.delete(optionalCuenta.get());
                        return Mono.empty();
                    } else {
                        return Mono.error(new NegocioException("Cuenta con ID " + id + " no encontrada para eliminar."));
                    }
                });
    }

    private Cuenta mapToEntity(CuentaDTO cuentaDTO) {
        return Cuenta.builder()
                .numeroCuenta(cuentaDTO.getNumeroCuenta())
                .tipoCuenta(cuentaDTO.getTipoCuenta())
                .saldoInicial(cuentaDTO.getSaldoInicial())
                .estado(cuentaDTO.getEstado())
                .build();
    }

    private CuentaResponseDTO mapToResponseDTO(Cuenta cuenta) {
        return CuentaResponseDTO.builder()
                .id(cuenta.getId())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(cuenta.getTipoCuenta())
                .saldoInicial(cuenta.getSaldoInicial())
                .estado(cuenta.getEstado())
                .build();
    }
}
