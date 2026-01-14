package cuenta_movimientos.com.service.impl;

import cuenta_movimientos.com.dto.response.ReporteResponseDTO;
import cuenta_movimientos.com.model.Cuenta;
import cuenta_movimientos.com.model.Movimiento;
import cuenta_movimientos.com.repository.interfaces.CuentaRepository;
import cuenta_movimientos.com.repository.interfaces.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReporteServiceImp {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    public static final String RETIRO = "RETIRO";
    public static final String PATRON_FECHA = "yyyy-MM-dd";

    @Transactional
    public List<ReporteResponseDTO> obtenerReportePorFecha(String fechaInicio, String fechaFin) {
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

        return reporteList;
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