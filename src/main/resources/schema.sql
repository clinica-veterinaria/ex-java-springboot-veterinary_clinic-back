-- Veterinary Clinic Database Schema
-- Created for Margarita's Veterinary Clinic Management System

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS treatments;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS users;

-- Users table for authentication and authorization
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15),
    dni VARCHAR(20) UNIQUE,
    role ENUM('ADMIN', 'CLIENT') NOT NULL DEFAULT 'CLIENT',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_dni (dni),
    INDEX idx_role (role)
);

-- Patients table
CREATE TABLE patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    breed VARCHAR(100) NOT NULL,
    gender ENUM('MALE', 'FEMALE') NOT NULL,
    species ENUM('DOG', 'CAT') NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_owner_id (owner_id),
    INDEX idx_species (species),
    
    CONSTRAINT chk_age CHECK (age > 0 AND age <= 30)
);

-- Treatments table
CREATE TABLE treatments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    treatment_name VARCHAR(200) NOT NULL,
    description TEXT,
    treatment_date DATE NOT NULL,
    cost DECIMAL(10, 2),
    veterinarian_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_treatment (patient_id),
    INDEX idx_treatment_date (treatment_date)
);

-- Appointments table
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    appointment_type ENUM('STANDARD', 'EMERGENCY') NOT NULL DEFAULT 'STANDARD',
    reason VARCHAR(500) NOT NULL,
    status ENUM('PENDING', 'ATTENDED', 'PAST') NOT NULL DEFAULT 'PENDING',
    notes TEXT,
    email_sent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_appointment_date (appointment_date),
    INDEX idx_patient_appointment (patient_id),
    INDEX idx_status (status),
    INDEX idx_appointment_datetime (appointment_date, appointment_time),
    
    -- Unique constraint to prevent double booking
    UNIQUE KEY unique_datetime (appointment_date, appointment_time),
    
    -- Check constraint for business rules
    CONSTRAINT chk_appointment_date CHECK (appointment_date >= CURDATE())
);

-- Trigger to automatically update appointment status to 'PAST'
DELIMITER //

CREATE TRIGGER update_past_appointments
    BEFORE UPDATE ON appointments
    FOR EACH ROW
BEGIN
    -- If appointment date has passed and status is still PENDING, change to PAST
    IF NEW.appointment_date < CURDATE() AND NEW.status = 'PENDING' THEN
        SET NEW.status = 'PAST';
    END IF;
END //

DELIMITER ;

-- Event to clean up old appointments (past appointments older than 3 months)
SET GLOBAL event_scheduler = ON;

DELIMITER //

CREATE EVENT IF NOT EXISTS cleanup_old_appointments
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    DELETE FROM appointments 
    WHERE status = 'PAST' 
    AND appointment_date < DATE_SUB(CURDATE(), INTERVAL 3 MONTH);
END //

DELIMITER ;

-- Function to check daily appointment limit (max 10 per day)
DELIMITER //

CREATE FUNCTION check_daily_appointment_limit(check_date DATE) 
RETURNS INT
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE appointment_count INT DEFAULT 0;
    
    SELECT COUNT(*) INTO appointment_count
    FROM appointments 
    WHERE appointment_date = check_date 
    AND status IN ('PENDING', 'ATTENDED');
    
    RETURN appointment_count;
END //

DELIMITER ;

-- Insert test data

-- Test users (owners and admin)
INSERT INTO users (username, email, password, first_name, last_name, phone_number, dni, role) VALUES
('admin', 'admin@vetclinic.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbCOkeMgF5PjBkqm2', 'Margarita', 'Rodriguez', '555-0001', '12345678A', 'ADMIN'),
('jperez', 'juan.perez@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbCOkeMgF5PjBkqm2', 'Juan', 'Perez', '555-0101', '87654321B', 'CLIENT'),
('mlopez', 'maria.lopez@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbCOkeMgF5PjBkqm2', 'Maria', 'Lopez', '555-0102', '11223344C', 'CLIENT'),
('carlos.martinez', 'carlos.martinez@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbCOkeMgF5PjBkqm2', 'Carlos', 'Martinez', '555-0103', '55667788D', 'CLIENT'),
('ana.garcia', 'ana.garcia@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbCOkeMgF5PjBkqm2', 'Ana', 'Garcia', '555-0104', '99887766E', 'CLIENT');

-- Test patients
INSERT INTO patients (patient_id, name, age, breed, gender, species, owner_id) VALUES
('PAT001', 'Max', 5, 'Golden Retriever', 'MALE', 'DOG', 2),
('PAT002', 'Luna', 3, 'Persian', 'FEMALE', 'CAT', 2),
('PAT003', 'Rocky', 7, 'German Shepherd', 'MALE', 'DOG', 3),
('PAT004', 'Mimi', 2, 'Siamese', 'FEMALE', 'CAT', 3),
('PAT005', 'Buddy', 4, 'Labrador', 'MALE', 'DOG', 4),
('PAT006', 'Whiskers', 6, 'Maine Coon', 'MALE', 'CAT', 4),
('PAT007', 'Bella', 1, 'Poodle', 'FEMALE', 'DOG', 5),
('PAT008', 'Shadow', 8, 'British Shorthair', 'MALE', 'CAT', 5);

-- Test treatments
INSERT INTO treatments (patient_id, treatment_name, description, treatment_date, cost, veterinarian_notes) VALUES
(1, 'Annual Vaccination', 'Complete vaccination package including rabies', '2024-01-15', 75.00, 'Patient responded well to vaccination'),
(1, 'Dental Cleaning', 'Professional dental cleaning and examination', '2024-03-20', 150.00, 'Some tartar buildup, recommended annual cleanings'),
(2, 'Spay Surgery', 'Ovariohysterectomy procedure', '2024-02-10', 300.00, 'Surgery successful, patient recovered well'),
(3, 'Hip X-Ray', 'X-ray examination for hip dysplasia screening', '2024-01-25', 120.00, 'No signs of dysplasia detected'),
(4, 'Annual Check-up', 'Routine health examination', '2024-02-05', 60.00, 'Healthy cat, no issues detected'),
(5, 'Skin Allergy Treatment', 'Treatment for allergic dermatitis', '2024-03-01', 90.00, 'Prescribed antihistamines and medicated shampoo');

-- Test appointments (mix of past, current and future)
INSERT INTO appointments (patient_id, appointment_date, appointment_time, appointment_type, reason, status, email_sent) VALUES
(1, '2024-12-20', '09:00:00', 'STANDARD', 'Annual check-up', 'PENDING', TRUE),
(2, '2024-12-20', '10:30:00', 'STANDARD', 'Follow-up after spay surgery', 'PENDING', TRUE),
(3, '2024-12-21', '14:00:00', 'EMERGENCY', 'Limping after exercise', 'PENDING', TRUE),
(4, '2024-12-22', '11:15:00', 'STANDARD', 'Vaccination booster', 'PENDING', FALSE),
(5, '2024-12-23', '16:30:00', 'STANDARD', 'Skin condition follow-up', 'PENDING', TRUE),
-- Past appointments
(6, '2024-09-15', '09:30:00', 'STANDARD', 'Routine examination', 'ATTENDED', TRUE),
(7, '2024-09-10', '14:15:00', 'STANDARD', 'Puppy first visit', 'ATTENDED', TRUE),
(8, '2024-08-20', '10:00:00', 'STANDARD', 'Senior cat check-up', 'PAST', TRUE);

-- Create indexes for better performance on common queries
CREATE INDEX idx_users_active ON users(is_active);
CREATE INDEX idx_patients_name ON patients(name);
CREATE INDEX idx_treatments_cost ON treatments(cost);
CREATE INDEX idx_appointments_email_sent ON appointments(email_sent);

-- Create a view for appointment details with patient and owner information
CREATE VIEW appointment_details AS
SELECT 
    a.id as appointment_id,
    a.appointment_date,
    a.appointment_time,
    a.appointment_type,
    a.reason,
    a.status,
    a.notes,
    p.patient_id,
    p.name as patient_name,
    p.species,
    p.breed,
    u.first_name as owner_first_name,
    u.last_name as owner_last_name,
    u.phone_number as owner_phone,
    u.email as owner_email
FROM appointments a
JOIN patients p ON a.patient_id = p.id
JOIN users u ON p.owner_id = u.id;

-- Create a view for patient treatment history
CREATE VIEW patient_treatment_history AS
SELECT 
    p.patient_id,
    p.name as patient_name,
    p.species,
    p.breed,
    t.treatment_name,
    t.description,
    t.treatment_date,
    t.cost,
    t.veterinarian_notes,
    u.first_name as owner_first_name,
    u.last_name as owner_last_name
FROM patients p
LEFT JOIN treatments t ON p.id = t.patient_id
JOIN users u ON p.owner_id = u.id
ORDER BY p.patient_id, t.treatment_date DESC;

-- Display table information
SHOW TABLES;

-- Display constraints and relationships
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_SCHEMA = DATABASE()
AND REFERENCED_TABLE_NAME IS NOT NULL;