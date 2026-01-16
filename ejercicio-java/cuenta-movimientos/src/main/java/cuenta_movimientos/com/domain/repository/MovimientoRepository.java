package cuenta_movimientos.com.domain.repository;

import cuenta_movimientos.com.domain.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaId(Long cuentaId);
    List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaAsc(Long cuentaId, Date fechaInicio, Date fechaFin);
}
