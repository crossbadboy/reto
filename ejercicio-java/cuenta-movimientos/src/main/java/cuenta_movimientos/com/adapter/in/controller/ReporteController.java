package cuenta_movimientos.com.adapter.in.controller;

import cuenta_movimientos.com.adapter.in.dto.response.ReporteResponseDTO;
import cuenta_movimientos.com.domain.port.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    private final ReporteService reporteService;

    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/porfecha")
    public Flux<ReporteResponseDTO> obtenerReportePorFecha(@RequestParam("fechaInicio") String fechaInicio,
                                                           @RequestParam("fechaFin") String fechaFin) {
        return reporteService.obtenerReportePorFecha(fechaInicio, fechaFin);
    }
}
