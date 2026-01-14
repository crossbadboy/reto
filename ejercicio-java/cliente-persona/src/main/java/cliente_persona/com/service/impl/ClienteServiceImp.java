package cliente_persona.com.service.impl;

import cliente_persona.com.dto.ClienteDTO;
import cliente_persona.com.dto.ClienteResponseDTO;
import cliente_persona.com.exception.NegocioException;
import cliente_persona.com.model.Cliente;
import cliente_persona.com.repository.interfaces.ClienteRepository;
import cliente_persona.com.service.ClienteProducer;
import cliente_persona.com.service.interfaces.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImp implements ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ClienteProducer clienteProducer;

    public ClienteResponseDTO saveCliente(ClienteDTO clienteDTO) {
        try {
            Optional<Cliente> clienteExistente = clienteRepository.findByClienteId(clienteDTO.getIdentificacion());
            if (clienteExistente.isPresent()) {
                throw new NegocioException("La identificación " + clienteDTO.getIdentificacion() + " ya está registrada.");
            }
            Cliente cliente = clienteRepository.save(mapToEntity(clienteDTO));
            clienteProducer.enviarMensajeCliente(cliente.getNombre());
            System.out.println("Mensaje enviado: " + cliente.getNombre());
            return mapToResponseDTO(cliente);
        } catch (Exception e) {
            throw new NegocioException("Error al guardar cliente: " + e.getMessage());
        }
    }

    public List<ClienteResponseDTO> getAllClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO  getClienteById(String id) {
        Cliente cliente = clienteRepository.findByClienteId(id)
                .orElseThrow(() -> new NegocioException("Cliente con ID " + id + " no encontrado."));
        return mapToResponseDTO(cliente);
    }

    public void deleteCliente(String id) {
        Optional<Cliente> clienteExistente = clienteRepository.findByClienteId(id);
        if (clienteExistente.isEmpty()) {
            throw new NegocioException("Cuenta con ID " + id + " no encontrada para eliminar.");
        }
        clienteRepository.delete(clienteExistente.get());
    }

    public ClienteResponseDTO updateCliente(String id, ClienteDTO clienteDTO) {
        Optional<Cliente> clienteExistente = clienteRepository.findByClienteId(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = mapToEntity(clienteDTO);
            cliente.setPersonaId(clienteExistente.get().getPersonaId());
            cliente = clienteRepository.save(cliente);
            return mapToResponseDTO(cliente);
        } else {
            throw new NegocioException("Cliente no encontrado con el id: " + id);
        }
    }

    private Cliente mapToEntity(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setGenero(clienteDTO.getGenero());
        cliente.setEdad(clienteDTO.getEdad());
        cliente.setIdentificacion(clienteDTO.getIdentificacion());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setClienteId(clienteDTO.getClienteId());
        cliente.setContrasena(clienteDTO.getContrasena());
        cliente.setEstado(clienteDTO.getEstado());
        return cliente;
    }

    private ClienteResponseDTO mapToResponseDTO(Cliente cliente) {
        ClienteResponseDTO responseDTO = new ClienteResponseDTO();
        responseDTO.setNombre(cliente.getNombre());
        responseDTO.setGenero(cliente.getGenero());
        responseDTO.setEdad(cliente.getEdad());
        responseDTO.setIdentificacion(cliente.getIdentificacion());
        responseDTO.setDireccion(cliente.getDireccion());
        responseDTO.setTelefono(cliente.getTelefono());
        responseDTO.setClienteId(cliente.getClienteId());
        responseDTO.setEstado(cliente.getEstado());
        responseDTO.setContrasena(cliente.getContrasena());
        return responseDTO;
    }

}
