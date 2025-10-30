# =========================================
# Stage 1: Build the Angular Application
# =========================================
FROM node:24.7.0-alpine AS frontend
WORKDIR /frontend
COPY client/package.json client/package-lock.json ./
RUN --mount=type=cache,target=/root/.npm npm ci
COPY client .
RUN npm run build -- --base-href / --output-path="dist" --configuration="production"

# =========================================
# Stage 2: Build the Java Application
# =========================================
FROM maven:4.0.0-rc-4-sapmachine-25 AS backend
WORKDIR /backend
COPY server/src /home/app/src
COPY server/pom.xml /home/app
# Embed the static files generated above
COPY --from=frontend /frontend/dist /home/app/src/main/resources/static
RUN mvn -f /home/app/pom.xml clean package
# =========================================
# Stage 3: Prepare executable
# =========================================
FROM openjdk:25-jdk as run
WORKDIR /app
COPY --from=backend /home/app/target/*.jar app.jar
EXPOSE 8989
ENTRYPOINT ["java","-jar","app.jar"]