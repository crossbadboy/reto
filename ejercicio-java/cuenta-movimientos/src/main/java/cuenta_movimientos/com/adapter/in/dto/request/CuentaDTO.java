package cuenta_movimientos.com.adapter.in.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaDTO {
    private String numeroCuenta;
    private String tipoCuenta;
    private Double saldoInicial;
    private Boolean estado;
}
