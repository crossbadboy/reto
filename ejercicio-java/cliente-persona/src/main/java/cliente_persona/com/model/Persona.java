package cliente_persona.com.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "persona")
public class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long personaId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "genero", nullable = false)
    private String genero;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "identificacion", nullable = false, unique = true)
    private String identificacion;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;
}
