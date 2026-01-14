package cliente_persona.com.controller;

import cliente_persona.com.dto.ClienteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteDTO buildClienteDTO(String nombre, String clienteId, String identificacion, int edad, String direccion, String telefono) {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre(nombre);
        clienteDTO.setGenero("F");
        clienteDTO.setEdad(edad);
        clienteDTO.setIdentificacion(identificacion);
        clienteDTO.setDireccion(direccion);
        clienteDTO.setTelefono(telefono);
        clienteDTO.setClienteId(clienteId);
        clienteDTO.setContrasena("password123");
        clienteDTO.setEstado(true);
        return clienteDTO;
    }

    private void crearClienteBase() throws Exception {
        ClienteDTO clienteDTO = buildClienteDTO(
            "Marianela", "CLI-001", "1234567894", 30, "Calle Ficticia 123", "123456789"
        );
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
            .andExpect(status().isCreated());
    }

    private void assertClienteResponse(String clienteId, String nombre, int edad) throws Exception {
        mockMvc.perform(get("/api/clientes/{id}", clienteId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.clienteId").value(clienteId))
            .andExpect(jsonPath("$.nombre").value(nombre))
            .andExpect(jsonPath("$.edad").value(edad));
    }

    @BeforeEach
    public void setUp() throws Exception {
        crearClienteBase();
    }

    @Test
    public void testCrearCliente() throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Maria Perez");
        clienteDTO.setGenero("F");
        clienteDTO.setEdad(28);
        clienteDTO.setIdentificacion("9999999999");
        clienteDTO.setDireccion("Calle Nueva 456");
        clienteDTO.setTelefono("555555555");
        clienteDTO.setClienteId("CLI-002");
        clienteDTO.setContrasena("password456");
        clienteDTO.setEstado(true);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Maria Perez"))
                .andExpect(jsonPath("$.genero").value("F"))
                .andExpect(jsonPath("$.edad").value(28))
                .andExpect(jsonPath("$.identificacion").value("9999999999"))
                .andExpect(jsonPath("$.direccion").value("Calle Nueva 456"))
                .andExpect(jsonPath("$.telefono").value("555555555"))
                .andExpect(jsonPath("$.clienteId").value("CLI-002"))
                .andExpect(jsonPath("$.estado").value(true));
    }

    @Test
    public void testGetAllClientes() throws Exception {
        mockMvc.perform(get("/api/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").isNotEmpty());
    }

    @Test
    public void testGetClienteById() throws Exception {
        assertClienteResponse("CLI-001", "Marianela", 30);
    }

    @Test
    public void testUpdateCliente() throws Exception {
        String clienteId = "CLI-001";
        ClienteDTO clienteDTO = buildClienteDTO(
            "Marianela Actualizada", clienteId, "1234567894", 32, "Calle Ficticia 123 Actualizada", "987654321"
        );
        mockMvc.perform(put("/api/clientes/{id}", clienteId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
            .andDo(print())
            .andExpect(status().isOk());
        assertClienteResponse("CLI-001", "Marianela Actualizada", 32);
    }

    @Test
    public void testDeleteCliente() throws Exception {
        String clienteId = "CLI-001";
        mockMvc.perform(delete("/api/clientes/{id}", clienteId)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent());
        // Verifica que ya no existe
        mockMvc.perform(get("/api/clientes/{id}", clienteId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }
}
