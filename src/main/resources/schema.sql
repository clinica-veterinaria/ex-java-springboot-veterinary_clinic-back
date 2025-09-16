-- =========================================
-- TABLA USUARIOS
-- =========================================
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    dni VARCHAR(20) UNIQUE, 
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    image BLOB 
);

-- =========================================
-- TABLA PACIENTES
-- =========================================
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    raza VARCHAR(100),
    genero ENUM('MACHO','HEMBRA') NOT NULL,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    tutor_nombre VARCHAR(150) NOT NULL,
    tutor_telefono VARCHAR(20) NOT NULL,
    image LONGBLOB
);
-- =========================================
-- TABLA CITAS
-- =========================================
CREATE TABLE IF NOT EXISTS citas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    paciente_id BIGINT NOT NULL,
    consulta ENUM('ESTANDAR','URGENCIA') NOT NULL,
    motivo VARCHAR(255),
    estado ENUM('PENDIENTE','ATENDIDA','PASADA') DEFAULT 'PENDIENTE',
    CONSTRAINT fk_cita_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE
);

-- =========================================
-- TABLA TRATAMIENTOS
-- =========================================

CREATE TABLE IF NOT EXISTS tratamientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    tratamiento VARCHAR(255) NOT NULL,
    fecha_tratamiento DATETIME NOT NULL,
    CONSTRAINT fk_tratamiento_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE
);

-- =========================================
-- ÍNDICES
-- =========================================
CREATE INDEX idx_pacientes_identificacion ON pacientes (identificacion);
CREATE INDEX idx_pacientes_tutor ON pacientes (tutor_nombre);
CREATE INDEX idx_citas_fecha_hora ON citas (fecha_hora);
CREATE INDEX idx_usuarios_email ON usuarios (email);
