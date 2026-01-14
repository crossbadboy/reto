package cliente_persona.com.service.interfaces;

import cliente_persona.com.dto.*;
import java.util.List;

public interface ClienteService {

    ClienteResponseDTO saveCliente(ClienteDTO cliente);

    List<ClienteResponseDTO > getAllClientes();

    ClienteResponseDTO getClienteById(String id);

    void deleteCliente(String id);

    ClienteResponseDTO updateCliente(String id, ClienteDTO clienteDTO);
}