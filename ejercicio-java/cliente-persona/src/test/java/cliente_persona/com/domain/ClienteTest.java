package cliente_persona.com.domain;

import cliente_persona.com.domain.model.Cliente;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {
    @Test
    void testGettersAndSetters() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Marianela");
        cliente.setGenero("F");
        cliente.setEdad(30);
        cliente.setIdentificacion("225487");
        cliente.setDireccion("Calle Falsa 123");
        cliente.setTelefono("123456789");
        cliente.setClienteId("CLI-001");
        cliente.setContrasena("secreta");
        cliente.setEstado(true);

        assertEquals("Marianela", cliente.getNombre());
        assertEquals("F", cliente.getGenero());
        assertEquals(30, cliente.getEdad());
        assertEquals("225487", cliente.getIdentificacion());
        assertEquals("Calle Falsa 123", cliente.getDireccion());
        assertEquals("123456789", cliente.getTelefono());
        assertEquals("CLI-001", cliente.getClienteId());
        assertEquals("secreta", cliente.getContrasena());
        assertTrue(cliente.getEstado());
    }
}
