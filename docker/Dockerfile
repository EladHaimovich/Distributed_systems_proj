FROM openjdk:11 AS builder

COPY . /build

WORKDIR /build

RUN mv src/server src/main && cp -r ./src/main/gradle/* . && ./gradlew build

FROM openjdk:11

RUN mkdir /server
COPY --from=builder /build/build/libs/SystemServer-0.0.1.jar /server
COPY --from=builder /build/docker/Entrypoint.sh /server

WORKDIR /server

RUN chmod +x ./Entrypoint.sh

ENTRYPOINT  bash ./Entrypoint.sh