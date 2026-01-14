package cliente_persona.com.service.impl;

import cliente_persona.com.dto.ClienteDTO;
import cliente_persona.com.dto.ClienteResponseDTO;
import cliente_persona.com.exception.NegocioException;
import cliente_persona.com.model.Cliente;
import cliente_persona.com.repository.interfaces.ClienteRepository;
import cliente_persona.com.service.ClienteProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceImpTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImp clienteServiceImp;

    @Mock
    private ClienteProducer clienteProducer;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cliente = new Cliente();
        cliente.setPersonaId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("M");
        cliente.setClienteId("1L");
        cliente.setEdad(30);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("098254785");
        cliente.setClienteId("1");
        cliente.setContrasena("123");
        cliente.setEstado(true);
    }

    @Test
    void saveCliente() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Jose Lema");
        clienteDTO.setIdentificacion("1234567890");
        clienteDTO.setGenero("M");
        clienteDTO.setEdad(30);
        clienteDTO.setDireccion("Otavalo sn y principal");
        clienteDTO.setTelefono("098254785");
        clienteDTO.setClienteId("1");
        clienteDTO.setContrasena("123");
        clienteDTO.setEstado(true);

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        doNothing().when(clienteProducer).enviarMensajeCliente(any(String.class));

        ClienteResponseDTO response = clienteServiceImp.saveCliente(clienteDTO);

        assertNotNull(response);
        assertEquals("1234567890", response.getIdentificacion());
        assertEquals("Jose Lema", response.getNombre());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void getAllClientes() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));

        List<ClienteResponseDTO> clientes = clienteServiceImp.getAllClientes();

        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        assertEquals("1234567890", clientes.get(0).getIdentificacion());
        verify(clienteRepository).findAll();
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void getClienteById() {
        when(clienteRepository.findByClienteId("2L")).thenReturn(Optional.of(cliente));

        ClienteResponseDTO response = clienteServiceImp.getClienteById("2L");

        assertNotNull(response);
        assertEquals("1234567890", response.getIdentificacion());
        verify(clienteRepository).findByClienteId("2L");
        verify(clienteRepository, times(1)).findByClienteId(any(String.class));
    }

    @Test
    void getCuentaById_notFound() {
        when(clienteRepository.findByClienteId("2L")).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> clienteServiceImp.getClienteById("99"));
    }

    @Test
    void deleteCliente() {
        when(clienteRepository.findByClienteId("1")).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(any(Cliente.class));

        clienteServiceImp.deleteCliente("1");

        verify(clienteRepository).findByClienteId("1");
        verify(clienteRepository).delete(any(Cliente.class));
        verify(clienteRepository, times(1)).findByClienteId(any(String.class));
        verify(clienteRepository, times(1)).delete(any(Cliente.class));
    }

    @Test
    void deleteCliente_notFound() {
        when(clienteRepository.findByClienteId("2")).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> clienteServiceImp.deleteCliente("2"));
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }

    @Test
    void updateCliente() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Andrea Robles");
        clienteDTO.setIdentificacion("1727366088");
        clienteDTO.setGenero("F");
        clienteDTO.setEdad(50);
        clienteDTO.setDireccion("Puembo calle juan leon mera");
        clienteDTO.setTelefono("098254785");
        clienteDTO.setClienteId("1");
        clienteDTO.setContrasena("123");
        clienteDTO.setEstado(true);

        when(clienteRepository.findByClienteId("2L")).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponseDTO clienteResponse = clienteServiceImp.updateCliente("2L", clienteDTO);

        assertNotNull(clienteResponse);
        assertEquals("1234567890", clienteResponse.getIdentificacion());
        verify(clienteRepository).findByClienteId("2L");
        verify(clienteRepository).save(any(Cliente.class));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(clienteRepository, times(1)).findByClienteId(any(String.class));
    }

    @Test
    void updateCuenta_notFound() {
        when(clienteRepository.findByClienteId("2L")).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> clienteServiceImp.updateCliente("2", new ClienteDTO()));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}