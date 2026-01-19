package cuenta_movimientos.com.adapter.in.controller;

import cuenta_movimientos.com.adapter.in.dto.response.ReporteResponseDTO;
import cuenta_movimientos.com.domain.port.ReportePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    private final ReportePort reportePort;

    @Autowired
    public ReporteController(ReportePort reportePort) {
        this.reportePort = reportePort;
    }

    @GetMapping("/porfecha")
    public Flux<ReporteResponseDTO> obtenerReportePorFecha(@RequestParam("fechaInicio") String fechaInicio,
                                                           @RequestParam("fechaFin") String fechaFin) {
        return reportePort.obtenerReportePorFecha(fechaInicio, fechaFin);
    }
}
