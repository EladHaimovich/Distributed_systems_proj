FROM openjdk:11

COPY . /server

WORKDIR /server

RUN ./gradlew build

RUN chmod +x ./docker/Entrypoint.sh

ENTRYPOINT  bash ./docker/Entrypoint.sh
