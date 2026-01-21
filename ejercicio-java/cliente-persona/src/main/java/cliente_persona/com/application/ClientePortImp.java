package cliente_persona.com.application;

import cliente_persona.com.adapter.in.web.dto.ClienteDTO;
import cliente_persona.com.adapter.in.web.dto.ClienteResponseDTO;
import cliente_persona.com.domain.exception.NegocioException;
import cliente_persona.com.domain.model.Cliente;
import cliente_persona.com.domain.port.ClientePort;
import cliente_persona.com.domain.repository.ClienteRepository;
import cliente_persona.com.domain.repository.PersonaRepository;
import cliente_persona.com.adapter.out.messaging.ClienteProducer;
import cliente_persona.com.adapter.mapper.ClienteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClientePortImp implements ClientePort {
    private static final Logger log = LoggerFactory.getLogger(ClientePortImp.class);
    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository;
    private final ClienteProducer clienteProducer;
    private final ClienteMapper clienteMapper;

    public ClientePortImp(ClienteRepository clienteRepository, PersonaRepository personaRepository, ClienteProducer clienteProducer, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.personaRepository = personaRepository;
        this.clienteProducer = clienteProducer;
        this.clienteMapper = clienteMapper;
    }

    @Override
    @Transactional
    public Mono<ClienteResponseDTO> saveCliente(ClienteDTO clienteDTO) {
        log.info("Intentando crear cliente con clienteId: {} e identificacion: {}", clienteDTO.getClienteId(), clienteDTO.getIdentificacion());
        return Mono.fromCallable(() -> {
            if (clienteRepository.findByClienteId(clienteDTO.getClienteId()).isPresent()) {
                log.warn("Intento de crear cliente duplicado con clienteId: {}", clienteDTO.getClienteId());
                throw new NegocioException("El clienteId " + clienteDTO.getClienteId() + " ya está registrado.");
            }
            if (personaRepository.findByIdentificacion(clienteDTO.getIdentificacion()).isPresent()) {
                log.warn("Intento de crear cliente con identificacion duplicada: {}", clienteDTO.getIdentificacion());
                throw new NegocioException("La identificación " + clienteDTO.getIdentificacion() + " ya está registrada.");
            }
            Cliente cliente = clienteMapper.toCliente(clienteDTO);
            Cliente savedCliente = clienteRepository.save(cliente);
            log.info("Cliente creado exitosamente: clienteId={}, nombre={}", savedCliente.getClienteId(), savedCliente.getNombre());
            clienteProducer.enviarMensajeCliente(savedCliente.getNombre());
            return clienteMapper.toClienteResponseDTO(savedCliente);
        });
    }

    @Override
    public Flux<ClienteResponseDTO> getAllClientes() {
        log.debug("Obteniendo todos los clientes");
        return Flux.fromIterable(clienteRepository.findAll())
                .map(clienteMapper::toClienteResponseDTO);
    }

    @Override
    public Mono<ClienteResponseDTO> getClienteById(String id) {
        log.debug("Buscando cliente por clienteId: {}", id);
        return Mono.fromCallable(() -> clienteRepository.findByClienteId(id))
                .flatMap(optionalCliente -> optionalCliente
                        .map(cliente -> {
                            log.info("Cliente encontrado: clienteId={}, nombre={}", cliente.getClienteId(), cliente.getNombre());
                            return Mono.just(clienteMapper.toClienteResponseDTO(cliente));
                        })
                        .orElseGet(() -> {
                            log.warn("Cliente no encontrado con clienteId: {}", id);
                            return Mono.error(new NegocioException("Cliente con ID " + id + " no encontrado."));
                        }));
    }

    @Override
    @Transactional
    public Mono<Void> deleteCliente(String id) {
        log.info("Intentando eliminar cliente con clienteId: {}", id);
        return Mono.fromCallable(() -> clienteRepository.findByClienteId(id))
                .flatMap(optionalCliente -> {
                    if (optionalCliente.isPresent()) {
                        clienteRepository.delete(optionalCliente.get());
                        log.info("Cliente eliminado: clienteId={}, nombre={}", id, optionalCliente.get().getNombre());
                        return Mono.empty();
                    } else {
                        log.warn("Intento de eliminar cliente no existente: clienteId={}", id);
                        return Mono.error(new NegocioException("Cliente con ID " + id + " no encontrado."));
                    }
                });
    }

    @Override
    @Transactional
    public Mono<ClienteResponseDTO> updateCliente(String id, ClienteDTO clienteDTO) {
        log.info("Intentando actualizar cliente con clienteId: {}", id);
        return Mono.fromCallable(() -> clienteRepository.findByClienteId(id))
                .flatMap(optionalCliente -> {
                    if (optionalCliente.isEmpty()) {
                        log.warn("Intento de actualizar cliente no existente: clienteId={}", id);
                        return Mono.error(new NegocioException("Cliente con ID " + id + " no encontrado."));
                    }
                    Cliente cliente = optionalCliente.get();
                    Cliente updatedCliente = clienteMapper.toCliente(clienteDTO);
                    updatedCliente.setId(cliente.getId());
                    Cliente savedCliente = clienteRepository.save(updatedCliente);
                    log.info("Cliente actualizado exitosamente: clienteId={}, nombre={}", savedCliente.getClienteId(), savedCliente.getNombre());
                    return Mono.just(clienteMapper.toClienteResponseDTO(savedCliente));
                });
    }
}
