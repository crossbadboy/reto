package cliente_persona.com.config;

import cliente_persona.com.model.Cliente;
import cliente_persona.com.repository.interfaces.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(ClienteRepository clienteRepository) {
        return args -> {
            Cliente cliente1 = new Cliente();
            cliente1.setNombre("Jose Lema");
            cliente1.setGenero("M");
            cliente1.setEdad(30);
            cliente1.setIdentificacion("1234567890");
            cliente1.setDireccion("Otavalo sn y principal");
            cliente1.setTelefono("098254785");
            cliente1.setClienteId("1L");
            cliente1.setContrasena("1NH45678&^%");
            cliente1.setEstado(true);

            Cliente cliente2 = new Cliente();
            cliente2.setNombre("Marianela Montalvo");
            cliente2.setGenero("F");
            cliente2.setEdad(25);
            cliente2.setIdentificacion("9876543210");
            cliente2.setDireccion("Amazonas y NNUU");
            cliente2.setTelefono("097548965");
            cliente2.setClienteId("2L");
            cliente2.setContrasena("78&^%1NH456");
            cliente2.setEstado(true);

            clienteRepository.save(cliente1);
            clienteRepository.save(cliente2);
        };
    }
}
