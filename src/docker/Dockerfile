FROM openjdk:11 AS builder

COPY . /build

WORKDIR /build

RUN apt update && apt install dos2unix

RUN mv src/server src/main 
RUN cp -r ./src/main/gradle/* . 

RUN dos2unix gradlew

RUN ./gradlew build 

FROM openjdk:11

RUN mkdir /server
COPY --from=builder /build/build/libs/SystemServer-0.0.1.jar /server
COPY --from=builder /build/src/docker/Entrypoint.sh /server

WORKDIR /server

RUN chmod +x ./Entrypoint.sh

ENTRYPOINT  bash ./Entrypoint.sh