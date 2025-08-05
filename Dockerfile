# Use an official Java image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mkdir -p src/main && mkdir -p src/test
RUN ./mvnw dependency:go-offline || true

# Copy all project files
COPY . .

# Build the application
RUN ./mvnw clean install -DskipTests

# Expose port
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "target/billingsoftware-0.0.1-SNAPSHOT.jar"]
