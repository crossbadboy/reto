package cliente_persona.com.model;

import cliente_persona.com.repository.interfaces.ClienteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class ClienteTest {

    @Autowired
    private ClienteRepository clienteRepository;

    Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setNombre("Marianela");
        cliente.setGenero("F");
        cliente.setEdad(30);
        cliente.setIdentificacion("225487");
        cliente.setDireccion("Calle Falsa 123");
        cliente.setTelefono("123456789");
        cliente.setClienteId("CLI-001");
        cliente.setContrasena("secreta");
        cliente.setEstado(true);
    }


    @Test
    void testGuardarYLeerCliente() {
        Cliente clienteGuardado = clienteRepository.save(cliente);

        assertNotNull(clienteGuardado.getPersonaId());
        assertEquals("CLI-001", clienteGuardado.getClienteId());
        assertEquals("Marianela", clienteGuardado.getNombre());
    }

    @Test
    void testGuardarClienteSinNombre() {
        cliente.setNombre(null);
        try {
            clienteRepository.save(cliente);
            fail("Lanzar la excepción si no lanzo en el metodo");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    @Test
    void testBuscarPorClienteId() {
        Cliente clienteGuardado = clienteRepository.save(cliente);

        Cliente clienteEncontrado = clienteRepository.findByClienteId(clienteGuardado.getClienteId()).orElse(null);

        assertNotNull(clienteEncontrado);
        assertEquals("Marianela", clienteEncontrado.getNombre());
    }

    @Test
    public void testToString() {
        Cliente cliente = new Cliente();
        cliente.setClienteId("CLI-001");
        cliente.setNombre("Marianela");

        String resultadoToString = cliente.toString();

        assertThat(resultadoToString).contains("CLI-001");
        assertThat(resultadoToString).contains("Marianela");
    }
}
