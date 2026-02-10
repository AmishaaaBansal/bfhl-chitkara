FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

# 👇 THIS LINE IS THE FIX
RUN chmod +x bfhl/mvnw
RUN cd bfhl && ./mvnw clean package -DskipTests


EXPOSE 8080

CMD ["java", "-jar", "target/bfhl-0.0.1-SNAPSHOT.jar"]
