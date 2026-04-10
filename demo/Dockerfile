# ════════════════════════════════════════════
# Stage 1 — Build avec Maven
# ════════════════════════════════════════════
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copier pom.xml séparément pour profiter du cache Docker
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copie le code source
COPY src ./src

# Build sans tests
RUN mvn package -DskipTests -q

# ════════════════════════════════════════════
# Stage 2 — Image légère JRE (runtime seulement)
# ════════════════════════════════════════════
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copier uniquement le jar depuis le stage builder
COPY --from=builder /app/target/demo-*.jar app.jar

# Répertoire pour les fichiers uploadés
RUN mkdir -p /app/uploads

# Port réel de l'application
EXPOSE 8082

# Lancement optimisé
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
