package cuenta_movimientos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@SpringBootApplication
@EntityScan(basePackages = "cuenta_movimientos.com.domain")
public class CuentaMovimientosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuentaMovimientosApplication.class, args);
	}

}
