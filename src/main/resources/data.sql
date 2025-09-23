-- =========================================
-- ADMIN USER (BCrypt password)
-- =========================================
INSERT INTO users (name, dni, email, password, role, image)
VALUES ('Administrator', '12345678Z', 'admin@olivia.com',
        '$2a$10$5vt5wq02vQZExo8WwIhD2e5UAnL9qONhxjWDwHFOuo4FzY9Hdf.9C', -- BCrypt of 'admin123'
        'ADMIN',
        NULL); 

-- =========================================
-- PATIENTS
-- =========================================
INSERT INTO patients (name, age, breed, gender, pet_identification, tutor_name, tutor_dni, tutor_phone, tutor_email, image)
VALUES
('Luna', 5, 'Golden Retriever', 'FEMALE', 'PET123456', 'Ana Pérez', '12345678A', '600123456', 'efrentomascampa@gmail.com', NULL),
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
