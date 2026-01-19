package cliente_persona.com.domain.port;

import cliente_persona.com.adapter.in.web.dto.ClienteDTO;
import cliente_persona.com.adapter.in.web.dto.ClienteResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientePort {
    Mono<ClienteResponseDTO> saveCliente(ClienteDTO clienteDTO);
    Flux<ClienteResponseDTO> getAllClientes();
    Mono<ClienteResponseDTO> getClienteById(String id);
    Mono<Void> deleteCliente(String id);
    Mono<ClienteResponseDTO> updateCliente(String id, ClienteDTO clienteDTO);
}

