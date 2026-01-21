package cliente_persona.com.adapter.mapper;

import cliente_persona.com.adapter.in.web.dto.ClienteDTO;
import cliente_persona.com.adapter.in.web.dto.ClienteResponseDTO;
import cliente_persona.com.domain.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "personaId", ignore = true)
    Cliente toCliente(ClienteDTO clienteDTO);

    ClienteResponseDTO toClienteResponseDTO(Cliente cliente);
}
