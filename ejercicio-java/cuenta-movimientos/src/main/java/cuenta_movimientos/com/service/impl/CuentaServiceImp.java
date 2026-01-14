package cuenta_movimientos.com.service.impl;

import cuenta_movimientos.com.dto.request.CuentaDTO;
import cuenta_movimientos.com.dto.response.CuentaResponseDTO;
import cuenta_movimientos.com.exception.NegocioException;
import cuenta_movimientos.com.model.Cuenta;
import cuenta_movimientos.com.repository.interfaces.CuentaRepository;
import cuenta_movimientos.com.service.interfaces.CuentaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaServiceImp implements CuentaService {

    private final CuentaRepository cuentaRepository;

    @Autowired
    public CuentaServiceImp(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    @Transactional
    public CuentaResponseDTO saveCuenta(CuentaDTO cuentaDTO) {
        Cuenta cuenta = mapToEntity(cuentaDTO);
        return mapToResponseDTO(cuentaRepository.save(cuenta));
    }

    @Override
    @Transactional
    public CuentaResponseDTO updateCuenta(Long id, CuentaDTO cuentaDTO) {
        return cuentaRepository.findById(id)
                .map(cuenta -> {
                    cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
                    cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
                    cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());
                    cuenta.setEstado(cuentaDTO.getEstado());
                    return mapToResponseDTO(cuentaRepository.save(cuenta));
                })
                .orElseThrow(() -> new NegocioException("Cuenta con ID " + id + " no encontrada para actualizar."));
    }

    @Override
    public List<CuentaResponseDTO> getAllCuentas() {
        return cuentaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public CuentaResponseDTO getCuentaById(Long id) {
        return mapToResponseDTO(cuentaRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Cuenta con ID " + id + " no encontrado.")));
    }

    @Override
    @Transactional
    public void deleteCuenta(Long id) {
        if (!cuentaRepository.existsById(id)) {
            throw new NegocioException("Cuenta con ID " + id + " no encontrada para eliminar.");
        }
        cuentaRepository.deleteById(id);
    }

    private Cuenta mapToEntity(CuentaDTO cuentaDTO) {
        return Cuenta.builder().estado(cuentaDTO.getEstado())
                .numeroCuenta(cuentaDTO.getNumeroCuenta()).tipoCuenta(cuentaDTO.getTipoCuenta())
                .saldoInicial(cuentaDTO.getSaldoInicial()).build();
    }

    private CuentaResponseDTO mapToResponseDTO(Cuenta cuenta) {
        return CuentaResponseDTO.builder().id(cuenta.getId())
                .numeroCuenta(cuenta.getNumeroCuenta()).tipoCuenta(cuenta.getTipoCuenta())
                .saldoInicial(cuenta.getSaldoInicial()).estado(cuenta.getEstado()).build();
    }
}
