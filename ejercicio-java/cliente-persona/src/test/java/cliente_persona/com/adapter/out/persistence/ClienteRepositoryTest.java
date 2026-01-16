package cliente_persona.com.adapter.out.persistence;

import cliente_persona.com.domain.model.Cliente;
import cliente_persona.com.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClienteRepositoryTest {
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testSaveAndFindByClienteId() {
        Cliente cliente = Cliente.builder()
            .nombre("Test")
            .genero("F")
            .edad(25)
            .identificacion("ID-TEST-001")
            .direccion("Calle Test 1")
            .telefono("555-1234")
            .clienteId("TEST-001")
            .contrasena("clave")
            .estado(true)
            .build();

        clienteRepository.save(cliente);

        Optional<Cliente> found = clienteRepository.findByClienteId("TEST-001");
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getNombre());
        assertEquals("ID-TEST-001", found.get().getIdentificacion());
    }
}
