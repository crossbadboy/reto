package cuenta_movimientos.com.service.impl;

import cuenta_movimientos.com.adapter.in.dto.request.MovimientoDTO;
import cuenta_movimientos.com.adapter.in.dto.response.MovimientoResponseDTO;
import cuenta_movimientos.com.application.ports.MovimientoPortImp;
import cuenta_movimientos.com.domain.exception.NegocioException;
import cuenta_movimientos.com.domain.model.Cuenta;
import cuenta_movimientos.com.domain.model.Movimiento;
import cuenta_movimientos.com.domain.repository.CuentaRepository;
import cuenta_movimientos.com.domain.repository.MovimientoRepository;
import cuenta_movimientos.com.mapper.MovimientoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MovimientoPortImpTest {
    @Mock
    private MovimientoRepository movimientoRepository;
    @Mock
    private CuentaRepository cuentaRepository;
    @Mock
    private MovimientoMapper movimientoMapper;
    @InjectMocks
    private MovimientoPortImp movimientoService;
    private Movimiento movimiento;
    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cuenta = Cuenta.builder().id(1L).numeroCuenta("1234567890").tipoCuenta("AHORRO")
                .saldoInicial(1000.00).estado(Boolean.TRUE).build();
        movimiento = Movimiento.builder().movimientoId(1L).tipoMovimiento("DEPOSITO").valor(100.0)
                .saldo(1100.0).cuenta(cuenta).fecha(new java.util.Date()).build();
        MovimientoResponseDTO movimientoResponseDTO = MovimientoResponseDTO.builder()
                .id(1L)
                .fecha(movimiento.getFecha().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                .tipoMovimiento("DEPOSITO")
                .valor(100.0)
                .saldo(1100.0)
                .cuentaId(1L)
                .numeroCuenta("1234567890")
                .build();
        Mockito.when(movimientoMapper.toDto(any(Movimiento.class))).thenReturn(movimientoResponseDTO);
    }

    @Test
    void createMovimiento() {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setTipoMovimiento("DEPOSITO");
        dto.setValor(100.0);
        dto.setCuentaId(1L);
        dto.setNumeroCuenta("1234567890");
        when(cuentaRepository.findByNumeroCuenta("1234567890")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);
        Mono<MovimientoResponseDTO> result = movimientoService.createMovimiento(dto);
        StepVerifier.create(result)
                .assertNext(resp -> {
                    assertNotNull(resp);
                    assertEquals("DEPOSITO", resp.getTipoMovimiento());
                })
                .verifyComplete();
    }

    @Test
    void updateMovimiento_notFound() {
        when(movimientoRepository.findById(2L)).thenReturn(Optional.empty());
        Mono<MovimientoResponseDTO> result = movimientoService.updateMovimiento("2", new MovimientoDTO());
        StepVerifier.create(result)
                .expectError(NegocioException.class)
                .verify();
    }

    @Test
    void getAllMovimientos() {
        when(movimientoRepository.findAll()).thenReturn(Arrays.asList(movimiento));
        Flux<MovimientoResponseDTO> result = movimientoService.getAllMovimientos();
        StepVerifier.create(result)
                .assertNext(resp -> assertEquals("DEPOSITO", resp.getTipoMovimiento()))
                .verifyComplete();
    }

    @Test
    void getMovimientoById() {
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));
        Mono<MovimientoResponseDTO> result = movimientoService.getMovimientoById("1");
        StepVerifier.create(result)
                .assertNext(resp -> assertEquals("DEPOSITO", resp.getTipoMovimiento()))
                .verifyComplete();
    }

    @Test
    void deleteMovimiento() {
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));
        doNothing().when(movimientoRepository).delete(any(Movimiento.class));
        Mono<Void> result = movimientoService.deleteMovimiento("1");
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void deleteMovimiento_notFound() {
        when(movimientoRepository.findById(2L)).thenReturn(Optional.empty());
        Mono<Void> result = movimientoService.deleteMovimiento("2");
        StepVerifier.create(result)
                .expectError(NegocioException.class)
                .verify();
    }
}
