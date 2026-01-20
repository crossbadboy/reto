package cuenta_movimientos.com.mapper;

import cuenta_movimientos.com.adapter.in.dto.response.MovimientoResponseDTO;
import cuenta_movimientos.com.domain.model.Movimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {
    @Mappings({
        @Mapping(target = "id", source = "movimientoId"),
        @Mapping(target = "fecha", expression = "java(toLocalDate(movimiento.getFecha()))"),
        @Mapping(target = "tipoMovimiento", source = "tipoMovimiento"),
        @Mapping(target = "valor", source = "valor"),
        @Mapping(target = "saldo", source = "saldo"),
        @Mapping(target = "cuentaId", source = "cuenta.id"),
        @Mapping(target = "numeroCuenta", source = "cuenta.numeroCuenta")
    })
    MovimientoResponseDTO toDto(Movimiento movimiento);

    default LocalDate toLocalDate(Date fecha) {
        return fecha == null ? null : fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
