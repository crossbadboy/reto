package cliente_persona.com.config;

import cliente_persona.com.domain.model.Cliente;
import cliente_persona.com.domain.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(ClienteRepository clienteRepository) {
        return args -> {
            Cliente cliente1 = Cliente.builder()
                .nombre("Jose Lema")
                .genero("M")
                .edad(30)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .clienteId("1L")
                .contrasena("1NH45678&^%")
                .estado(true)
                .build();

            Cliente cliente2 = Cliente.builder()
                .nombre("Marianela Montalvo")
                .genero("F")
                .edad(25)
                .identificacion("9876543210")
                .direccion("Amazonas y NNUU")
                .telefono("097548965")
                .clienteId("2L")
                .contrasena("78&^%1NH456")
                .estado(true)
                .build();

            clienteRepository.save(cliente1);
            clienteRepository.save(cliente2);
        };
    }
}
