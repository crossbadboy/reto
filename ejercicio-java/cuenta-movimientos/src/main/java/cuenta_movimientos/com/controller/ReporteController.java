package cuenta_movimientos.com.controller;

import cuenta_movimientos.com.dto.response.ReporteResponseDTO;
import cuenta_movimientos.com.service.impl.ReporteServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteServiceImp reporteService;

    @GetMapping("/porfecha")
    public List<ReporteResponseDTO> obtenerReportePorFecha(@RequestParam("fechaInicio") String fechaInicio,
                                                           @RequestParam("fechaFin") String fechaFin) {
        return reporteService.obtenerReportePorFecha(fechaInicio, fechaFin);
    }
}
