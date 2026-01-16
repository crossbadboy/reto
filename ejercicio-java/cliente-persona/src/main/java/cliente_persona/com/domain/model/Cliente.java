package cliente_persona.com.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "persona_id")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends Persona implements Serializable {
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}
