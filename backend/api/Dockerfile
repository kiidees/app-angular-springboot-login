# Imagen oficial y perfectamente compatible con Spring Boot 3
FROM eclipse-temurin:21-jdk

WORKDIR /usr/src/app

# Copiamos todos los archivos del backend
COPY . /usr/src/app

# Otorgamos permisos de ejecución al empaquetador de Maven
RUN chmod +x ./mvnw

# Compilación limpia inyectando la bandera para evitar el bug "this.hashes"
RUN ./mvnw clean install -DskipTests -e -Dmaven.compiler.forceJavacCompilerUse=true

CMD ["./mvnw", "spring-boot:run", "-Dmaven.compiler.forceJavacCompilerUse=true"]
