# see also: 
# docker-compose.yml, there is the execution and stuff defined

# docker-compose.override.yml this overrides certain definitions in the docker-compose, 
# which are relevant for the local test environment but not the productive server.
# (by building on server, it is not copied)

# initialize build-container (deleted after docker build)
FROM openjdk:17-jdk-slim AS build
WORKDIR /app

# copy from outside-folder to inside-folder
COPY . .
# install gradle (auto genrated file)
RUN ./gradlew build --stacktrace


# initialize productive container (this is the result of the build)
FROM openjdk:17-slim AS release
WORKDIR /app

# important for container management
ARG BUILD_VERSION=0.1.0
ARG GIT_COMMIT=unknown

# standard lables for containers 
LABEL org.opencontainers.image.version="${BUILD_VERSION}"\
      org.opencontainers.image.revision="${GIT_COMMIT}"

# readable during container runntime (inside and outside of container)
ENV APP_VERSION ${BUILD_VERSION}
ENV APP_DB_URL jdbc:postgresql://db:5432/nsctool
ENV APP_DB_USERNAME nsctool
ENV APP_DB_PASSWORD nsctool

# exposed port
EXPOSE 8080

# copy from build container (not outside of docker)
COPY --from=build /app/build/libs/api-*.jar /app/api.jar
# executes this command (the main command of this docker, only one command!)
CMD ["java", "-jar", "/app/api.jar"]
