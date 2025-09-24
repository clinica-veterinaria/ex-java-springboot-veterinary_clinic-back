#!/bin/bash

# Ruta de tu proyecto
PROJECT_PATH="/c/Users/Equipo/OneDrive/Escritorio/clinica-veterinaria-back/ex-java-springboot-veterinary_clinic-back"

# Nombre del servicio MySQL en docker-compose.yml
MYSQL_SERVICE_NAME="mysql"

# Ir al proyecto
cd "$PROJECT_PATH" || exit

echo "Verificando estado de los contenedores Docker..."
# Comprobar si el contenedor MySQL ya está corriendo
if [ "$(docker ps -q -f name=$MYSQL_SERVICE_NAME)" ]; then
    echo "Docker ya está corriendo. No se necesita levantarlo."
else
    echo "Docker no está corriendo. Arrancando contenedores..."
    docker-compose up -d
fi

echo "Esperando a que MySQL esté listo..."
# Loop para esperar hasta que MySQL acepte conexiones
until docker exec "$MYSQL_SERVICE_NAME" mysqladmin ping -h "$MYSQL_SERVICE_NAME" --silent; do
    echo "Esperando MySQL..."
    sleep 3
done

echo "MySQL listo. Iniciando Spring Boot..."
# Aquí asumiendo que usas Maven
mvn spring-boot:run



# ./start-backend.sh