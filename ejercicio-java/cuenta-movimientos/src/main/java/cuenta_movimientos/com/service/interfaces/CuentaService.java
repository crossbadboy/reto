package cuenta_movimientos.com.service.interfaces;

import cuenta_movimientos.com.dto.request.CuentaDTO;
import cuenta_movimientos.com.dto.response.CuentaResponseDTO;

import java.util.List;

public interface CuentaService {

    CuentaResponseDTO saveCuenta(CuentaDTO cuentaDTO);

    CuentaResponseDTO updateCuenta(Long id, CuentaDTO cuentaDTO);

    List<CuentaResponseDTO> getAllCuentas();

    CuentaResponseDTO getCuentaById(Long id);

    void deleteCuenta(Long id);
}
