#FROM ubuntu:16.04
FROM openjdk:11

ARG ShardID
ARG ServerID

ENV shardid=ShardID

RUN wget https://archive.apache.org/dist/zookeeper/zookeeper-3.4.9/zookeeper-3.4.9.tar.gz
RUN tar -xvzf zookeeper-3.4.9.tar.gz

COPY . /server

WORKDIR /server



RUN echo "$ShardID $ServerID" > ./parameters

EXPOSE 20000 30000 40000

#RUN ls ./build/libs

RUN pwd && ls .

ENTRYPOINT java -jar ./build/libs/SystemServer-0.0.1.jar
