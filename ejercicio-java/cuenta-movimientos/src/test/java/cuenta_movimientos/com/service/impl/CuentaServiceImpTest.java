package cuenta_movimientos.com.service.impl;

import cuenta_movimientos.com.dto.request.CuentaDTO;
import cuenta_movimientos.com.dto.response.CuentaResponseDTO;
import cuenta_movimientos.com.exception.NegocioException;
import cuenta_movimientos.com.model.Cuenta;
import cuenta_movimientos.com.repository.interfaces.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CuentaServiceImpTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaServiceImp cuentaService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cuenta = Cuenta.builder().id(1L).numeroCuenta("1234567890").tipoCuenta("AHORRO")
                .saldoInicial(1000.00).estado(Boolean.TRUE).build();
    }

    @Test
    void saveCuenta() {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setNumeroCuenta("1234567890");
        cuentaDTO.setTipoCuenta("AHORRO");
        cuentaDTO.setSaldoInicial(1000.00);
        cuentaDTO.setEstado(true);

        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        CuentaResponseDTO response = cuentaService.saveCuenta(cuentaDTO);

        assertNotNull(response);
        assertEquals("1234567890", response.getNumeroCuenta());
        assertEquals("AHORRO", response.getTipoCuenta());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void updateCuenta() {
        CuentaDTO cuentaDto = new CuentaDTO();
        cuentaDto.setNumeroCuenta("1111222233");
        cuentaDto.setTipoCuenta("CORRIENTE");
        cuentaDto.setSaldoInicial(1500.00);
        cuentaDto.setEstado(false);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        CuentaResponseDTO updated = cuentaService.updateCuenta(1L, cuentaDto);

        assertNotNull(updated);
        assertEquals("1111222233", updated.getNumeroCuenta());
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).save(any(Cuenta.class));
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
        verify(cuentaRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void updateCuenta_notFound() {
        when(cuentaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> cuentaService.updateCuenta(2L, new CuentaDTO()));
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void getAllCuentas() {
        when(cuentaRepository.findAll()).thenReturn(Arrays.asList(cuenta));

        List<CuentaResponseDTO> cuentas = cuentaService.getAllCuentas();

        assertNotNull(cuentas);
        assertEquals(1, cuentas.size());
        assertEquals("1234567890", cuentas.get(0).getNumeroCuenta());
        verify(cuentaRepository).findAll();
        verify(cuentaRepository, times(1)).findAll();
    }

    @Test
    void getCuentaById() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        CuentaResponseDTO response = cuentaService.getCuentaById(1L);

        assertNotNull(response);
        assertEquals("1234567890", response.getNumeroCuenta());
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository, times(1)).findById(any(Long.class));
    }


    @Test
    void getCuentaById_notFound() {
        when(cuentaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> cuentaService.getCuentaById(99L));
    }

    @Test
    void deleteCuenta() {
        when(cuentaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cuentaRepository).deleteById(1L);

        cuentaService.deleteCuenta(1L);

        verify(cuentaRepository).existsById(1L);
        verify(cuentaRepository).deleteById(1L);
        verify(cuentaRepository, times(1)).existsById(any(Long.class));
        verify(cuentaRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void deleteCuenta_notFound() {
        when(cuentaRepository.existsById(2L)).thenReturn(false);

        assertThrows(NegocioException.class, () -> cuentaService.deleteCuenta(2L));
        verify(cuentaRepository, never()).deleteById(anyLong());
    }
}