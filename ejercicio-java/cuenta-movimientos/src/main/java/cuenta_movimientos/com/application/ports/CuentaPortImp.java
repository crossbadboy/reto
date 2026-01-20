package cuenta_movimientos.com.application.ports;

import cuenta_movimientos.com.adapter.in.dto.request.CuentaDTO;
import cuenta_movimientos.com.adapter.in.dto.response.CuentaResponseDTO;
import cuenta_movimientos.com.domain.exception.NegocioException;
import cuenta_movimientos.com.domain.model.Cuenta;
import cuenta_movimientos.com.domain.repository.CuentaRepository;
import cuenta_movimientos.com.domain.port.CuentaPort;
import cuenta_movimientos.com.mapper.CuentaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CuentaPortImp implements CuentaPort {
    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Autowired
    public CuentaPortImp(CuentaRepository cuentaRepository, CuentaMapper cuentaMapper) {
        this.cuentaRepository = cuentaRepository;
        this.cuentaMapper = cuentaMapper;
    }

    @Override
    @Transactional
    public Mono<CuentaResponseDTO> saveCuenta(CuentaDTO cuentaDTO) {
        return Mono.fromCallable(() -> {
            Cuenta cuenta = cuentaMapper.toEntity(cuentaDTO);
            Cuenta saved = cuentaRepository.save(cuenta);
            return cuentaMapper.toDto(saved);
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
                    Cuenta cuenta = updateCuentaFromDto(optionalCuenta.get(), cuentaDTO);
                    return Mono.just(cuentaMapper.toDto(cuentaRepository.save(cuenta)));
                });
    }

    @Override
    public Flux<CuentaResponseDTO> getAllCuentas() {
        return Flux.defer(() -> Flux.fromIterable(cuentaRepository.findAll())
                .map(cuentaMapper::toDto));
    }

    @Override
    public Mono<CuentaResponseDTO> getCuentaById(String id) {
        return Mono.fromCallable(() -> cuentaRepository.findById(Long.valueOf(id)))
                .flatMap(optionalCuenta -> optionalCuenta
                        .map(cuenta -> Mono.just(cuentaMapper.toDto(cuenta)))
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

    private Cuenta updateCuentaFromDto(Cuenta cuenta, CuentaDTO cuentaDTO) {
        cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuenta.setEstado(cuentaDTO.getEstado());
        return cuenta;
    }
}
