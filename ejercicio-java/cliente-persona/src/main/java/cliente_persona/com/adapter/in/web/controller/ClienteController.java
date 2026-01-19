package cliente_persona.com.adapter.in.web.controller;

import cliente_persona.com.adapter.in.web.dto.ClienteDTO;
import cliente_persona.com.adapter.in.web.dto.ClienteResponseDTO;
import cliente_persona.com.domain.port.ClientePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClientePort clientePort;

    @Autowired
    public ClienteController(ClientePort clientePort) {
        this.clientePort = clientePort;
    }

    @GetMapping
    public Flux<ClienteResponseDTO> getAllClientes() {
        return clientePort.getAllClientes();
    }

    @GetMapping("/{id}")
    public Mono<ClienteResponseDTO> getClienteById(@PathVariable String id) {
        return clientePort.getClienteById(id);
    }

    @PostMapping
    public Mono<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        return clientePort.saveCliente(clienteDTO);
    }

    @PutMapping("/{id}")
    public Mono<ClienteResponseDTO> updateCliente(@PathVariable String id, @Valid @RequestBody ClienteDTO clienteDTO) {
        return clientePort.updateCliente(id, clienteDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCliente(@PathVariable String id) {
        return clientePort.deleteCliente(id);
    }
}
