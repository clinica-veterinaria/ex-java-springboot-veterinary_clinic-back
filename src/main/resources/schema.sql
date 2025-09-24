-- =========================================
-- TABLE USERS
-- =========================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dni VARCHAR(20) UNIQUE, 
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    image BLOB 
);

-- =========================================
-- TABLE PATIENTS
-- =========================================
CREATE TABLE IF NOT EXISTS patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    breed VARCHAR(100),
    gender ENUM('MALE','FEMALE') NOT NULL,
    pet_identification VARCHAR(50) UNIQUE NOT NULL,
    tutor_name VARCHAR(150) NOT NULL,
    tutor_dni VARCHAR(50) UNIQUE NOT NULL,
    tutor_phone VARCHAR(20) NOT NULL,
    tutor_email VARCHAR(150) UNIQUE NOT NULL,
    image LONGBLOB
);

-- =========================================
-- TABLE APPOINTMENTS
-- =========================================
CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_datetime DATETIME NOT NULL,
    patient_id BIGINT NOT NULL,
    type ENUM('STANDARD','URGENT') NOT NULL,
    reason VARCHAR(255),
    status ENUM('PENDING','ATTENDED','PAST') DEFAULT 'PENDING',
    CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- =========================================
-- TABLE TREATMENTS
-- =========================================
CREATE TABLE IF NOT EXISTS treatments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    treatment VARCHAR(255) NOT NULL,
    treatment_date DATETIME NOT NULL,
    CONSTRAINT fk_treatment_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- =========================================
-- INDEXES
-- =========================================
DROP INDEX IF EXISTS idx_patients_pet_identification ON patients;
CREATE INDEX idx_patients_pet_identification ON patients (pet_identification);

DROP INDEX IF EXISTS idx_patients_tutor_name ON patients;
CREATE INDEX idx_patients_tutor_name ON patients (tutor_name);

DROP INDEX IF EXISTS idx_patients_tutor_dni ON patients;
CREATE INDEX idx_patients_tutor_dni ON patients (tutor_dni);

DROP INDEX IF EXISTS idx_appointments_datetime ON appointments;
CREATE INDEX idx_appointments_datetime ON appointments (appointment_datetime);

DROP INDEX IF EXISTS idx_users_email ON users;
CREATE INDEX idx_users_email ON users (email);
