package cuenta_movimientos.com.domain.port;

import cuenta_movimientos.com.adapter.in.dto.request.MovimientoDTO;
import cuenta_movimientos.com.adapter.in.dto.response.MovimientoResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovimientoService {
    Mono<MovimientoResponseDTO> createMovimiento(MovimientoDTO movimientoDTO);
    Mono<MovimientoResponseDTO> updateMovimiento(String id, MovimientoDTO movimientoDTO);
    Flux<MovimientoResponseDTO> getAllMovimientos();
    Mono<MovimientoResponseDTO> getMovimientoById(String id);
    Mono<Void> deleteMovimiento(String id);
}
