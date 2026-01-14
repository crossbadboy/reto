DROP TABLE IF EXISTS movimiento;
DROP TABLE IF EXISTS cuenta;
CREATE TABLE cuenta (
    cuenta_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(15, 2) NOT NULL,
    estado BOOLEAN NOT NULL
);

CREATE TABLE movimiento (
    movimiento_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    saldo DECIMAL(15, 2) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    CONSTRAINT fk_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta(cuenta_id) ON DELETE CASCADE
);

