# Clínica Veterinaria Backend

Este proyecto es el backend de una aplicación para la gestión de una clínica veterinaria, desarrollado con Java Spring Boot y MySQL.

## Características principales
- Gestión de usuarios con roles (ADMIN, USER)
- Autenticación básica (Basic Auth)
- CRUD de pacientes, citas (appointments), tratamientos y usuarios
- Endpoints protegidos por rol
- Configuración CORS para integración con frontend
- Pruebas y ejemplos listos para Postman

## Estructura de carpetas
```
├── src/main/java/org/digital_academy/
│   ├── appointment/
│   ├── config/
│   ├── patient/
│   ├── treatment/
│   ├── user/
│   └── util/
├── src/main/resources/
│   ├── application.properties
│   ├── data.sql
│   ├── schema.sql
│   └── ...
```

## Instalación y ejecución
1. Clona el repositorio:
	 ```
	 git clone https://github.com/<usuario>/<repositorio>.git
	 ```
2. Configura la base de datos MySQL (ver `application.properties`).
3. Levanta los servicios con Docker Compose:
	 ```
	 docker compose up -d
	 ```
4. Ejecuta la aplicación Spring Boot:
	 ```
	 mvn spring-boot:run
	 ```

## Endpoints principales
- **Autenticación:**
	- `POST /auth/login` (Basic Auth)
	- `POST /auth/register`
- **Usuarios:**
	- `GET /users`
	- `POST /users`
	- `PUT /users/{id}`
	- `DELETE /users/{id}`
- **Pacientes:**
	- `GET /patients`
	- `POST /patients`
	- ...
- **Citas:**
	- `GET /appointments`
	- `POST /appointments`
	- ...
- **Tratamientos:**
	- `GET /treatments`
	- `POST /treatments`
	- ...

## Pruebas en Postman
- Usa la autenticación básica para endpoints protegidos.
- Ejemplo de login:
	- Usuario: `admin`
	- Contraseña: `admin123`

## Contribuir
1. Haz un fork del repositorio.
2. Crea una rama para tu feature/fix.
3. Haz tus cambios y crea un pull request.


## diagrama de clases UML

> Proyecto desarrollado por Digital Academy para la gestión de clínicas veterinarias.

@startuml
class UserEntity {
  - id: Long
  - username: String
  - password: String
  - roles: Set<String>
  - name: String
  - dni: String
  - email: String
  - telefono: String
}

class Patient {
  - id: Long
  - name: String
  - age: Integer
  - breed: String
  - gender: String
  - petIdentification: String
  - tutorName: String
  - tutorDni: String
  - tutorPhone: String
  - tutorEmail: String
}

class Appointment {
  - id: Long
  - appointmentDatetime: LocalDateTime
  - type: String
  - status: String
  - reason: String
  - patient: Patient
}

class Treatment {
  - id: Long
  - treatment: String
  - medication: String
  - dosage: Double
  - treatmentDate: LocalDateTime
  - patient: Patient
}

UserEntity "1" -- "many" Appointment : crea
Patient "1" -- "many" Appointment : tiene
Patient "1" -- "many" Treatment : recibe

@enduml


---

> Proyecto desarrollado por Efren, Paula, Sara, Dima y Alberto para la gestión de clínicas veterinarias.