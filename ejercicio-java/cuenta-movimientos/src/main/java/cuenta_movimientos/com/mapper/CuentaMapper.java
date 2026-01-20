package cuenta_movimientos.com.mapper;

import cuenta_movimientos.com.adapter.in.dto.request.CuentaDTO;
import cuenta_movimientos.com.adapter.in.dto.response.CuentaResponseDTO;
import cuenta_movimientos.com.domain.model.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CuentaMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numeroCuenta", source = "numeroCuenta")
    @Mapping(target = "tipoCuenta", source = "tipoCuenta")
    @Mapping(target = "saldoInicial", source = "saldoInicial")
    @Mapping(target = "estado", source = "estado")
    CuentaResponseDTO toDto(Cuenta cuenta);

    @Mapping(target = "numeroCuenta", source = "numeroCuenta")
    @Mapping(target = "tipoCuenta", source = "tipoCuenta")
    @Mapping(target = "saldoInicial", source = "saldoInicial")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    Cuenta toEntity(CuentaDTO cuentaDTO);
}
