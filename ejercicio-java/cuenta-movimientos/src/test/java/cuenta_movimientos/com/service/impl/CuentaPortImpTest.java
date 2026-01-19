package cuenta_movimientos.com.service.impl;

import cuenta_movimientos.com.adapter.in.dto.request.CuentaDTO;
import cuenta_movimientos.com.adapter.in.dto.response.CuentaResponseDTO;
import cuenta_movimientos.com.application.ports.CuentaPortImp;
import cuenta_movimientos.com.domain.exception.NegocioException;
import cuenta_movimientos.com.domain.model.Cuenta;
import cuenta_movimientos.com.domain.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CuentaPortImpTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaPortImp cuentaService;

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

        Mono<CuentaResponseDTO> result = cuentaService.saveCuenta(cuentaDTO);
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals("1234567890", response.getNumeroCuenta());
                    assertEquals("AHORRO", response.getTipoCuenta());
                })
                .verifyComplete();
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

        Mono<CuentaResponseDTO> result = cuentaService.updateCuenta("1", cuentaDto);
        StepVerifier.create(result)
                .assertNext(updated -> {
                    assertNotNull(updated);
                    assertEquals("1111222233", updated.getNumeroCuenta());
                })
                .verifyComplete();
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).save(any(Cuenta.class));
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
        verify(cuentaRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void updateCuenta_notFound() {
        when(cuentaRepository.findById(2L)).thenReturn(Optional.empty());
        Mono<CuentaResponseDTO> result = cuentaService.updateCuenta("2", new CuentaDTO());
        StepVerifier.create(result)
                .expectError(NegocioException.class)
                .verify();
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void getAllCuentas() {
        when(cuentaRepository.findAll()).thenReturn(Arrays.asList(cuenta));
        Flux<CuentaResponseDTO> result = cuentaService.getAllCuentas();
        StepVerifier.create(result)
                .assertNext(cuentas -> {
                    assertNotNull(cuentas);
                    assertEquals("1234567890", cuentas.getNumeroCuenta());
                })
                .verifyComplete();
        verify(cuentaRepository).findAll();
        verify(cuentaRepository, times(1)).findAll();
    }

    @Test
    void getCuentaById() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        Mono<CuentaResponseDTO> result = cuentaService.getCuentaById("1");
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals("1234567890", response.getNumeroCuenta());
                })
                .verifyComplete();
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void getCuentaById_notFound() {
        when(cuentaRepository.findById(99L)).thenReturn(Optional.empty());
        Mono<CuentaResponseDTO> result = cuentaService.getCuentaById("99");
        StepVerifier.create(result)
                .expectError(NegocioException.class)
                .verify();
    }

    @Test
    void deleteCuenta() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        doNothing().when(cuentaRepository).delete(any(Cuenta.class));
        Mono<Void> result = cuentaService.deleteCuenta("1");
        StepVerifier.create(result)
                .verifyComplete();
        verify(cuentaRepository).findById(1L);
        verify(cuentaRepository).delete(any(Cuenta.class));
    }

    @Test
    void deleteCuenta_notFound() {
        when(cuentaRepository.findById(2L)).thenReturn(Optional.empty());
        Mono<Void> result = cuentaService.deleteCuenta("2");
        StepVerifier.create(result)
                .expectError(NegocioException.class)
                .verify();
        verify(cuentaRepository, never()).delete(any(Cuenta.class));
    }
}