#FROM ubuntu:16.04
FROM openjdk:11

#ARG ShardID
#ARG ServerID
#ARG ZookeeperPort

#ENV shardid=ShardID

#RUN wget https://archive.apache.org/dist/zookeeper/zookeeper-3.4.9/zookeeper-3.4.9.tar.gz
#RUN tar -xvzf zookeeper-3.4.9.tar.gz
#COPY src/main/resources/zoo.cfg  /zookeeper-3.4.9/conf/
#RUN echo '99' > myid
#RUN mkdir /ZooKeeper/
#RUN mkdir /ZooKeeper/data
#RUN mv ./myid /ZooKeeper/data/myid



COPY . /server

WORKDIR /server



#RUN #echo "$ShardID $ServerID $ZookeeperPort" > ./parameters && cat ./parameters

#EXPOSE 30000 30100 40000 40100

#RUN ls ./build/libs

RUN pwd && ls .

RUN chmod +x ./docker/Entrypoint.sh

ENTRYPOINT  bash ./docker/Entrypoint.sh
