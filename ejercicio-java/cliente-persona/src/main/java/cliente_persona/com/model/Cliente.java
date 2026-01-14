package cliente_persona.com.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@PrimaryKeyJoinColumn(name = "persona_id")
@Table(name = "cliente")
public class Cliente extends Persona implements Serializable {

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getPersonaId() +
                ", nombre='" + getNombre() + '\'' +
                ", genero='" + getGenero() + '\'' +
                ", edad=" + getEdad() +
                ", clienteId=" + clienteId +
                ", identificacion='" + getIdentificacion() + '\'' +
                ", direccion='" + getDireccion() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", estado=" + estado +
                '}';
    }
}