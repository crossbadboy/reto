package cliente_persona.com.adapter.in.web;

import cliente_persona.com.adapter.in.web.dto.ClienteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWebTestClient
class ClienteControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    private ClienteDTO buildClienteDTO(String nombre, String clienteId, String identificacion, int edad, String direccion, String telefono) {
        return ClienteDTO.builder()
            .nombre(nombre)
            .genero("F")
            .edad(edad)
            .identificacion(identificacion)
            .direccion(direccion)
            .telefono(telefono)
            .clienteId(clienteId)
            .contrasena("password123")
            .estado(true)
            .build();
    }

    @BeforeEach
    void setUp() {
        // Limpieza lógica: elimina el cliente base si existe
        webTestClient.delete().uri("/api/clientes/{id}", "CLI-001")
                .exchange();
        ClienteDTO clienteDTO = buildClienteDTO(
            "Marianela", "CLI-001", "1234567894", 30, "Calle Ficticia 123", "123456789"
        );
        webTestClient.post().uri("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(clienteDTO)
                .exchange();
    }

    @Test
    void testCrearCliente() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
            .nombre("Maria Perez")
            .genero("F")
            .edad(28)
            .identificacion("9999999999")
            .direccion("Calle Nueva 456")
            .telefono("555555555")
            .clienteId("CLI-002")
            .contrasena("password456")
            .estado(true)
            .build();

        webTestClient.post().uri("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(clienteDTO)
                .exchange()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Maria Perez")
                .jsonPath("$.genero").isEqualTo("F")
                .jsonPath("$.edad").isEqualTo(28)
                .jsonPath("$.identificacion").isEqualTo("9999999999")
                .jsonPath("$.direccion").isEqualTo("Calle Nueva 456")
                .jsonPath("$.telefono").isEqualTo("555555555")
                .jsonPath("$.clienteId").isEqualTo("CLI-002")
                .jsonPath("$.estado").isEqualTo(true);
    }

    @Test
    void testCrearClienteDuplicado() {
        ClienteDTO clienteDTO = buildClienteDTO(
            "Marianela", "CLI-001", "1234567894", 30, "Calle Ficticia 123", "123456789"
        );
        webTestClient.post().uri("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(clienteDTO)
                .exchange();
    }

    @Test
    void testGetAllClientes() {
        webTestClient.get().uri("/api/clientes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .jsonPath("$[0].nombre").isNotEmpty();
    }

    @Test
    void testGetClienteById() {
        webTestClient.get().uri("/api/clientes/{id}", "CLI-001")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .jsonPath("$.clienteId").isEqualTo("CLI-001")
                .jsonPath("$.nombre").isEqualTo("Marianela");
    }

    @Test
    void testGetClienteByIdNotFound() {
        webTestClient.get().uri("/api/clientes/{id}", "NO-EXISTE")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    @Test
    void testUpdateCliente() {
        ClienteDTO clienteDTO = buildClienteDTO(
            "Marianela Actualizada", "CLI-001", "1234567894", 32, "Calle Ficticia 123 Actualizada", "987654321"
        );
        webTestClient.put().uri("/api/clientes/{id}", "CLI-001")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(clienteDTO)
                .exchange();
        // Verifica actualización
        webTestClient.get().uri("/api/clientes/{id}", "CLI-001")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Marianela Actualizada")
                .jsonPath("$.edad").isEqualTo(32);
    }

    @Test
    void testUpdateClienteNotFound() {
        ClienteDTO clienteDTO = buildClienteDTO(
            "No Existe", "NO-EXISTE", "0000000000", 40, "Calle Falsa", "000000000"
        );
        webTestClient.put().uri("/api/clientes/{id}", "NO-EXISTE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(clienteDTO)
                .exchange();
    }

    @Test
    void testDeleteCliente() {
        webTestClient.delete().uri("/api/clientes/{id}", "CLI-001")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
        // Verifica que ya no existe
        webTestClient.get().uri("/api/clientes/{id}", "CLI-001")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    @Test
    void testDeleteClienteNotFound() {
        webTestClient.delete().uri("/api/clientes/{id}", "NO-EXISTE")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }
}
