package cuenta_movimientos.com.adapter.in.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoDTO {
    private String tipoMovimiento;
    private Double valor;
    private Long cuentaId;
    private String numeroCuenta;
}
