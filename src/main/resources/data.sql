-- =========================================
-- ADMIN USER (BCrypt password)
-- =========================================
-- Plain password: admin123
INSERT INTO users (name, dni, email, password, role, image)
VALUES ('Administrator', '12345678Z', 'admin@olivia.com',
        '$2a$10$5vt5wq02vQZExo8WwIhD2e5UAnL9qONhxjWDwHFOuo4FzY9Hdf.9C', -- BCrypt of 'admin123'
        'ADMIN',
        NULL); 

-- =========================================
-- PATIENTS
-- =========================================
INSERT INTO patients (name, age, breed, gender, identification, tutor_name, tutor_phone, tutor_email)
VALUES
('Luna', 5, 'Golden Retriever', 'FEMALE', '12345678A', 'Ana Pérez', '600123456', 'efrentomascampa@gmail.com'),
('Simba', 4, 'Siamese', 'MALE', '87654321B', 'Carlos García', '600654321', 'carlos.garcia@gmail.com'),
('Milo', 3, 'French Bulldog', 'MALE', '45678912C', 'Lucía Fernández', '600789123', 'lucia.fernandez@gmail.com'),
('Nala', 6, 'Mixed', 'FEMALE', '78912345D', 'Javier López', '600987321', 'javier.lopez@gmail.com');

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
