-- =========================================
-- TABLE USERS
-- =========================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50),
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_role FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================
-- DATOS INICIALES
-- =========================================
-- Admin (password: admin123)
INSERT INTO users (id, username, email, password)
VALUES (1, 'margarita', 'margarita@oliwa.com', '$2a$10$7Q6c7A0VZyN1j8H8vwrNNOiKQqT7HcbMOnm.6Zbsz4PzZQGd1hY8e')
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO user_roles (user_id, role)
VALUES (1, 'ADMIN')
ON DUPLICATE KEY UPDATE role=role;

-- Usuario de prueba (password: user123)
INSERT INTO users (id, username, email, password)
VALUES (2, 'usuario', 'user@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO user_roles (user_id, role)
VALUES (2, 'USER')
ON DUPLICATE KEY UPDATE role=role;


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
CREATE INDEX idx_patients_pet_identification ON patients (pet_identification);
CREATE INDEX idx_patients_tutor_name ON patients (tutor_name);
CREATE INDEX idx_patients_tutor_dni ON patients (tutor_dni);
CREATE INDEX idx_appointments_datetime ON appointments (appointment_datetime);
CREATE INDEX idx_appointments_status ON appointments (status);
CREATE INDEX idx_treatments_patient_date ON treatments (patient_id, treatment_date);