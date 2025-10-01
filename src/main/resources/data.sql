-- =========================================
-- ADMIN USER (BCrypt password)
-- =========================================
INSERT INTO users (id, username, email, password, name, dni, phone)
VALUES (1, 'margarita@oliwa.com', 'margarita@oliwa.com', 
        '$2a$10$w0Y.etYN84xCI2Rzb9kS9OuyiSG5PFNYAnGz0WQQfo3WpNtOr.HV.',
        'Margarita Admin', '12345678A', '666777888')
ON DUPLICATE KEY UPDATE 
    username = VALUES(username),
    email = VALUES(email),
    password = VALUES(password),
    name = VALUES(name),
    dni = VALUES(dni),
    phone = VALUES(phone);

INSERT INTO user_roles (user_id, role)
VALUES (1, 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE role = VALUES(role);

-- Test user (email: user@test.com, password: user123)
INSERT INTO users (id, username, email, password, name, dni, phone)
VALUES (2, 'user@test.com', 'user@test.com', 
        '$2a$10$/mBzhZVa7WZV/38OTQ9tQ.CrTLRZaGOu1hTslst3NgTl9KeYhbIRa',
        'Usuario Prueba', '87654321B', '666888999')
ON DUPLICATE KEY UPDATE 
    username = VALUES(username),
    email = VALUES(email),
    password = VALUES(password),
    name = VALUES(name),
    dni = VALUES(dni),
    phone = VALUES(phone);

INSERT INTO user_roles (user_id, role)
VALUES (2, 'ROLE_USER')
ON DUPLICATE KEY UPDATE role = VALUES(role);


-- =========================================
-- PATIENTS
-- =========================================
INSERT INTO patients (name, age, breed, gender, pet_identification, tutor_name, tutor_dni, tutor_phone, tutor_email, image)
VALUES
('Luna', 5, 'Golden Retriever', 'FEMALE', 'PET123456', 'Ana Pérez', '12345678A', '600123456', 'paulafa8@gmail.com', NULL),
('Simba', 4, 'Siamese', 'MALE', 'PET654321', 'Carlos García', '87654321B', '600654321', 'carlos.garcia@gmail.com', NULL),
('Milo', 3, 'French Bulldog', 'MALE', 'PET789123', 'Lucía Fernández', '45678912C', '600789123', 'lucia.fernandez@gmail.com', NULL),
('Nala', 6, 'Mixed', 'FEMALE', 'PET321987', 'Javier López', '78912345D', '600987321', 'javier.lopez@gmail.com', NULL);

-- =========================================
-- APPOINTMENTS
-- =========================================
INSERT INTO appointments (appointment_datetime, patient_id, type, reason, status)
VALUES
('2025-09-20 10:00:00', 1, 'STANDARD', 'Annual vaccination', 'PENDING'),
('2025-09-22 16:30:00', 2, 'STANDARD', 'General checkup', 'PENDING'),
('2025-09-25 09:15:00', 3, 'URGENT', 'Quarterly deworming', 'PENDING');

-- =========================================
-- TREATMENTS
-- =========================================
INSERT INTO treatments (patient_id, treatment, treatment_date)
VALUES
(1, 'Rabies vaccine', '2025-01-15 10:00:00'),
(1, 'Internal deworming', '2025-03-10 09:00:00'),
(2, 'Dental check', '2025-02-20 14:30:00'),
(3, 'Tetravalent vaccine', '2025-04-05 11:00:00'),
(4, 'Weight control', '2025-05-12 12:00:00');
