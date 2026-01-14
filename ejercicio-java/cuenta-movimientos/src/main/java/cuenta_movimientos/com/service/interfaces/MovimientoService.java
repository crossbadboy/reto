package cuenta_movimientos.com.service.interfaces;

import cuenta_movimientos.com.dto.request.MovimientoDTO;
import cuenta_movimientos.com.dto.response.MovimientoResponseDTO;

import java.util.List;

public interface MovimientoService {

    MovimientoResponseDTO createMovimiento(MovimientoDTO movimientoDTO);

    MovimientoResponseDTO updateMovimiento(Long id, MovimientoDTO movimientoDTO);

    List<MovimientoResponseDTO> getAllMovimientos();

    MovimientoResponseDTO getMovimientoById(Long id);

    void deleteMovimiento(Long id);
}
