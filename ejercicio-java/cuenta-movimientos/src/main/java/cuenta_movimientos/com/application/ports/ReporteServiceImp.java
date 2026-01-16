package cuenta_movimientos.com.application.ports;

import cuenta_movimientos.com.adapter.in.dto.response.ReporteResponseDTO;
import cuenta_movimientos.com.domain.model.Movimiento;
import cuenta_movimientos.com.domain.repository.CuentaRepository;
import cuenta_movimientos.com.domain.repository.MovimientoRepository;
import cuenta_movimientos.com.domain.port.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReporteServiceImp implements ReporteService {
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    @Autowired
    public ReporteServiceImp(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public static final String RETIRO = "RETIRO";
    public static final String PATRON_FECHA = "yyyy-MM-dd";

    @Override
    public Flux<ReporteResponseDTO> obtenerReportePorFecha(String fechaInicio, String fechaFin) {
        return Flux.defer(() -> {
            Date inicio = parseFecha(fechaInicio);
            Date fin = parseFecha(fechaFin);
            List<ReporteResponseDTO> reporteList = new ArrayList<>();
            cuentaRepository.findAll().forEach(cuenta -> {
                Double saldo = cuenta.getSaldoInicial();
                List<Movimiento> movimientos = movimientoRepository
                        .findByCuentaIdAndFechaBetweenOrderByFechaAsc(cuenta.getId(), inicio, fin);
                for (Movimiento movimiento : movimientos) {
                    Double valor = RETIRO.equalsIgnoreCase(movimiento.getTipoMovimiento()) ?
                            -movimiento.getValor() : movimiento.getValor();
                    saldo += valor;
                    reporteList.add(ReporteResponseDTO.builder()
                            .fecha(formatFecha(movimiento.getFecha()))
                            .cliente(cuenta.getNumeroCuenta())
                            .numeroCuenta(cuenta.getNumeroCuenta())
                            .tipo(cuenta.getTipoCuenta())
                            .saldoInicial(cuenta.getSaldoInicial())
                            .estado(cuenta.getEstado())
                            .movimiento(valor)
                            .saldoDisponible(saldo)
                            .build());
                }
            });
            return Flux.fromIterable(reporteList);
        });
    }

    private Date parseFecha(String fecha) {
        try {
            return new SimpleDateFormat(PATRON_FECHA).parse(fecha);
        } catch (ParseException e) {
            throw new RuntimeException("Fecha inválida.");
        }
    }

    private String formatFecha(Date fecha) {
        return new SimpleDateFormat(PATRON_FECHA).format(fecha);
    }
}
