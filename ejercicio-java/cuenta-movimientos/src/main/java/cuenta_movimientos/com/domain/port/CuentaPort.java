package cuenta_movimientos.com.domain.port;

import cuenta_movimientos.com.adapter.in.dto.request.CuentaDTO;
import cuenta_movimientos.com.adapter.in.dto.response.CuentaResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CuentaPort {
    Mono<CuentaResponseDTO> saveCuenta(CuentaDTO cuentaDTO);
    Mono<CuentaResponseDTO> updateCuenta(String id, CuentaDTO cuentaDTO);
    Flux<CuentaResponseDTO> getAllCuentas();
    Mono<CuentaResponseDTO> getCuentaById(String id);
    Mono<Void> deleteCuenta(String id);
}
