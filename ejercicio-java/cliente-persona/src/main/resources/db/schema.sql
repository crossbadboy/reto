DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS persona;
CREATE TABLE persona (
     persona_id BIGINT AUTO_INCREMENT PRIMARY KEY,
     nombre VARCHAR(100) NOT NULL,
     genero VARCHAR(50) NOT NULL,
     edad INT NOT NULL,
     identificacion VARCHAR(50) NOT NULL UNIQUE,
     direccion VARCHAR(255),
     telefono VARCHAR(50)
);

CREATE TABLE cliente (
     persona_id BIGINT NOT NULL PRIMARY KEY,
     cliente_id VARCHAR(50) NOT NULL UNIQUE,
     contrasena VARCHAR(100) NOT NULL,
     estado BOOLEAN NOT NULL,
     CONSTRAINT fk_persona FOREIGN KEY (persona_id) REFERENCES persona(persona_id) ON DELETE CASCADE
);

