package cliente_persona.com.application;

import cliente_persona.com.adapter.in.web.dto.ClienteDTO;
import cliente_persona.com.adapter.in.web.dto.ClienteResponseDTO;
import cliente_persona.com.domain.exception.NegocioException;
import cliente_persona.com.domain.model.Cliente;
import cliente_persona.com.domain.port.ClientePort;
import cliente_persona.com.domain.repository.ClienteRepository;
import cliente_persona.com.domain.repository.PersonaRepository;
import cliente_persona.com.adapter.out.messaging.ClienteProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientePortImp implements ClientePort {
    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository;
    private final ClienteProducer clienteProducer;

    public ClientePortImp(ClienteRepository clienteRepository, PersonaRepository personaRepository, ClienteProducer clienteProducer) {
        this.clienteRepository = clienteRepository;
        this.personaRepository = personaRepository;
        this.clienteProducer = clienteProducer;
    }

    private Cliente buildClienteFromDTO(ClienteDTO clienteDTO) {
        return Cliente.builder()
                .nombre(clienteDTO.getNombre())
                .genero(clienteDTO.getGenero())
                .edad(clienteDTO.getEdad())
                .identificacion(clienteDTO.getIdentificacion())
                .direccion(clienteDTO.getDireccion())
                .telefono(clienteDTO.getTelefono())
                .contrasena(clienteDTO.getContrasena())
                .estado(clienteDTO.getEstado())
                .clienteId(clienteDTO.getClienteId())
                .build();
    }

    @Override
    @Transactional
    public Mono<ClienteResponseDTO> saveCliente(ClienteDTO clienteDTO) {
        return Mono.fromCallable(() -> {
            if (clienteRepository.findByClienteId(clienteDTO.getClienteId()).isPresent()) {
                throw new NegocioException("El clienteId " + clienteDTO.getClienteId() + " ya está registrado.");
            }
            if (personaRepository.findByIdentificacion(clienteDTO.getIdentificacion()).isPresent()) {
                throw new NegocioException("La identificación " + clienteDTO.getIdentificacion() + " ya está registrada.");
            }
            Cliente cliente = buildClienteFromDTO(clienteDTO);
            Cliente savedCliente = clienteRepository.save(cliente);
            clienteProducer.enviarMensajeCliente(savedCliente.getNombre());
            return mapToResponseDTO(savedCliente);
        });
    }

    @Override
    public Flux<ClienteResponseDTO> getAllClientes() {
        return Flux.fromIterable(clienteRepository.findAll())
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<ClienteResponseDTO> getClienteById(String id) {
        return Mono.fromCallable(() -> clienteRepository.findByClienteId(id))
                .flatMap(optionalCliente -> optionalCliente
                        .map(cliente -> Mono.just(mapToResponseDTO(cliente)))
                        .orElseGet(() -> Mono.error(new NegocioException("Cliente con ID " + id + " no encontrado."))));
    }

    @Override
    @Transactional
    public Mono<Void> deleteCliente(String id) {
        return Mono.fromCallable(() -> clienteRepository.findByClienteId(id))
                .flatMap(optionalCliente -> {
                    if (optionalCliente.isPresent()) {
                        clienteRepository.delete(optionalCliente.get());
                        return Mono.empty();
                    } else {
                        return Mono.error(new NegocioException("Cliente con ID " + id + " no encontrado."));
                    }
                });
    }

    @Override
    @Transactional
    public Mono<ClienteResponseDTO> updateCliente(String id, ClienteDTO clienteDTO) {
        return Mono.fromCallable(() -> clienteRepository.findByClienteId(id))
                .flatMap(optionalCliente -> {
                    if (optionalCliente.isEmpty()) {
                        return Mono.error(new NegocioException("Cliente con ID " + id + " no encontrado."));
                    }
                    Cliente cliente = optionalCliente.get();
                    Cliente updatedCliente = buildClienteFromDTO(clienteDTO);
                    updatedCliente.setId(cliente.getId());
                    Cliente savedCliente = clienteRepository.save(updatedCliente);
                    return Mono.just(mapToResponseDTO(savedCliente));
                });
    }

    private ClienteResponseDTO mapToResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .nombre(cliente.getNombre())
                .genero(cliente.getGenero())
                .edad(cliente.getEdad())
                .identificacion(cliente.getIdentificacion())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .estado(cliente.getEstado())
                .clienteId(cliente.getClienteId())
                .contrasena(cliente.getContrasena())
                .build();
    }
}
