package cliente_persona.com.application;

import cliente_persona.com.domain.model.Cliente;
import cliente_persona.com.domain.repository.ClienteRepository;
import cliente_persona.com.adapter.in.web.dto.ClienteDTO;
import cliente_persona.com.adapter.in.web.dto.ClienteResponseDTO;
import cliente_persona.com.domain.exception.NegocioException;
import cliente_persona.com.adapter.out.messaging.ClienteProducer;
import cliente_persona.com.domain.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceImpTest {
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private PersonaRepository personaRepository;
    @InjectMocks
    private ClienteServiceImp clienteServiceImp;
    @Mock
    private ClienteProducer clienteProducer;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = Cliente.builder()
            .personaId(1L)
            .nombre("Jose Lema")
            .genero("M")
            .clienteId("1L")
            .edad(30)
            .identificacion("1234567890")
            .direccion("Otavalo sn y principal")
            .telefono("098254785")
            .contrasena("123")
            .estado(true)
            .build();
    }

    @Test
    void saveCliente() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
            .nombre("Jose Lema")
            .identificacion("1234567890")
            .genero("M")
            .edad(30)
            .direccion("Otavalo sn y principal")
            .telefono("098254785")
            .clienteId("1")
            .contrasena("123")
            .estado(true)
            .build();

        when(clienteRepository.findByClienteId("1")).thenReturn(Optional.empty());
        when(personaRepository.findByIdentificacion("1234567890")).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        ClienteResponseDTO response = clienteServiceImp.saveCliente(clienteDTO).block();

        assertNotNull(response);
        assertEquals("1234567890", response.getIdentificacion());
        assertEquals("Jose Lema", response.getNombre());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void getAllClientes() {
        when(clienteRepository.findAll()).thenReturn(Collections.singletonList(cliente));
        List<ClienteResponseDTO> clientes = clienteServiceImp.getAllClientes().collectList().block();
        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        assertEquals("1234567890", clientes.get(0).getIdentificacion());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void getClienteById() {
        when(clienteRepository.findByClienteId("2L")).thenReturn(Optional.of(cliente));
        ClienteResponseDTO response = clienteServiceImp.getClienteById("2L").block();
        assertNotNull(response);
        assertEquals("1234567890", response.getIdentificacion());
        verify(clienteRepository, times(1)).findByClienteId(any(String.class));
    }

    @Test
    void getCuentaById_notFound() {
        when(clienteRepository.findByClienteId("2L")).thenReturn(Optional.empty());
        assertThrows(NegocioException.class, () -> clienteServiceImp.getClienteById("99").block());
    }

    @Test
    void deleteCliente() {
        when(clienteRepository.findByClienteId("1")).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(any(Cliente.class));
        clienteServiceImp.deleteCliente("1").block();
        verify(clienteRepository, times(1)).findByClienteId(any(String.class));
        verify(clienteRepository, times(1)).delete(any(Cliente.class));
    }

    @Test
    void deleteCliente_notFound() {
        when(clienteRepository.findByClienteId("2")).thenReturn(Optional.empty());
        assertThrows(NegocioException.class, () -> clienteServiceImp.deleteCliente("2").block());
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }

    @Test
    void updateCliente() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
            .nombre("Andrea Robles")
            .identificacion("1727366088")
            .genero("F")
            .edad(50)
            .direccion("Puembo calle juan leon mera")
            .telefono("098254785")
            .clienteId("1")
            .contrasena("123")
            .estado(true)
            .build();

        when(clienteRepository.findByClienteId("1")).thenReturn(Optional.of(cliente));
        // Usar thenAnswer para devolver el objeto actualizado
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ClienteResponseDTO clienteResponse = clienteServiceImp.updateCliente("1", clienteDTO).block();
        assertNotNull(clienteResponse);
        assertEquals("1727366088", clienteResponse.getIdentificacion());
        verify(clienteRepository, times(1)).findByClienteId(any(String.class));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void updateCuenta_notFound() {
        ClienteDTO clienteDTO = ClienteDTO.builder().build();
        when(clienteRepository.findByClienteId("2L")).thenReturn(Optional.empty());
        assertThrows(NegocioException.class, () -> clienteServiceImp.updateCliente("2", clienteDTO).block());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}
