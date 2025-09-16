-- =========================================
-- USUARIO ADMIN (BCrypt password)
-- =========================================
-- Contraseña en texto plano: admin123
INSERT INTO usuarios (nombre, dni, email, password, rol, image)
VALUES ('Administrador', '12345678Z', 'admin@olivia.com',
        '$2a$10$5vt5wq02vQZExo8WwIhD2e5UAnL9qONhxjWDwHFOuo4FzY9Hdf.9C', -- BCrypt de 'admin123'
        'ADMIN',
        NULL); 

-- =========================================
-- PACIENTES
-- =========================================
INSERT INTO pacientes (nombre, edad, raza, genero, identificacion, tutor_nombre, tutor_telefono)
VALUES
('Luna', 5, 'Golden Retriever', 'HEMBRA', '12345678A', 'Ana Pérez', '600123456'),
('Simba', 4, 'Siames', 'MACHO', '87654321B', 'Carlos García', '600654321'),
('Milo', 3, 'Bulldog Francés', 'MACHO', '45678912C', 'Lucía Fernández', '600789123'),
('Nala', 6, 'Mestizo', 'HEMBRA', '78912345D', 'Javier López', '600987321');
-- =========================================
-- CITAS
-- =========================================
INSERT INTO citas (fecha_hora, paciente_id, consulta, motivo, estado)
VALUES
('2025-09-20 10:00:00', 1, 'ESTANDAR', 'Vacunación anual', 'PENDIENTE'),
('2025-09-22 16:30:00', 2, 'ESTANDAR', 'Revisión general', 'PENDIENTE'),
('2025-09-25 09:15:00', 3, 'URGENCIA', 'Desparasitación trimestral', 'PENDIENTE');

-- =========================================
-- TRATAMIENTOS
-- =========================================
INSERT INTO tratamientos (paciente_id, tratamiento, fecha_tratamiento)
VALUES
(1, 'Vacuna antirrábica', '2025-01-15 10:00:00'),
(1, 'Desparasitación interna', '2025-03-10 09:00:00'),
(2, 'Revisión dental', '2025-02-20 14:30:00'),
(3, 'Vacuna tetravalente', '2025-04-05 11:00:00'),
(4, 'Control de peso', '2025-05-12 12:00:00');
