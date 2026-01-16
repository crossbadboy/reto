package cuenta_movimientos.com.domain.port;

import cuenta_movimientos.com.adapter.in.dto.response.ReporteResponseDTO;
import reactor.core.publisher.Flux;

public interface ReporteService {
    Flux<ReporteResponseDTO> obtenerReportePorFecha(String fechaInicio, String fechaFin);
}

