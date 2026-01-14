package cuenta_movimientos.com.config;

import cuenta_movimientos.com.model.Cuenta;
import cuenta_movimientos.com.model.Movimiento;
import cuenta_movimientos.com.repository.interfaces.CuentaRepository;
import cuenta_movimientos.com.repository.interfaces.MovimientoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        return args -> {
            Cuenta cuenta1 = Cuenta.builder().numeroCuenta("1234567890").tipoCuenta("AHORRO")
                    .saldoInicial(1000.00).estado(Boolean.TRUE).build();
            Cuenta cuenta2 = Cuenta.builder().numeroCuenta("0987654321").tipoCuenta("CORRIENTE")
                    .saldoInicial(2500.00).estado(Boolean.TRUE).build();
            cuentaRepository.save(cuenta1);
            cuentaRepository.save(cuenta2);

            Movimiento movimiento1 = Movimiento.builder().tipoMovimiento("DEPOSITO").valor(500.00)
                    .saldo(1500.00).fecha(new Date()).cuenta(cuenta1).build();;
            Movimiento movimiento2 = Movimiento.builder().tipoMovimiento("RETIRO").valor(200.00)
                    .saldo(1300.00).fecha(new Date()).cuenta(cuenta1).build();
            Movimiento movimiento3 = Movimiento.builder().tipoMovimiento("DEPOSITO").valor(1000.00)
                    .saldo(3500.00).fecha(new Date()).cuenta(cuenta2).build();


            movimientoRepository.save(movimiento1);
            movimientoRepository.save(movimiento2);
            movimientoRepository.save(movimiento3);
        };
    }
}
